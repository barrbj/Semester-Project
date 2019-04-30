import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Controller for Card24Game FXML scene
 */
public class Cards24GameController {

    /**
     * FXML Variables
     */

    @FXML
    private Pane rootPane;

    @FXML
    private Button findASolutionBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button verifyBtn;

    @FXML
    private TextField expressionTextField;

    @FXML
    private TextField solutionTextField;

    @FXML
    private Pane cardPane;

    @FXML
    private Text time;

    /**
     * The timer for the elapsed time of a game.
     */
    private Timer timer = new Timer();

    /**
     * The amount of attempts that a user takes to complete a game
     */
    private int attempts = 0;

    /**
     * The directory that contains all of the card textures
     */
    private File[] cardDir = new File("png").listFiles();

    /**
     * The engine that provides Javascript scripting functions
     */
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * The list that contains every possible orders that the cards available can be in
     */
    private List<Integer[]> possibleNumberOrders = new ArrayList<>();

    /**
     * The list that contains every possible orders that operators can be in
     */
    private List<String[]> possibleOperatorOrders = new ArrayList<>();

    /**
     * The logger instance which is used to log things to a log file
     */
    private Logger logger = new Logger();

    /**
     * Initializes the scene for the card game, which includes the loading of solutions, setting the close request, fading in the scene, calling the refresh method which loads in the cards, and setting the verify and find a solution buttons to their corresponding functions
     */
    @FXML
    public void initialize() {
        findASolutionBtn.setText("Loading Solutions");
        findASolutionBtn.setDisable(true);
        Task loadingTask = new Task() {
            @Override
            protected Object call() {
                //generateOperatorOrders(new ArrayList<>(Arrays.asList("+", "-", "*", "/")), new Stack<>(), 3);
                generateOperatorOrders(new ArrayList<>(Arrays.asList("+", "-", "*", "/", "+", "-", "*", "/", "+", "-", "*", "/")), new Stack<>(), 3);
                return null;
            }
        };
        loadingTask.setOnSucceeded(event -> {
            findASolutionBtn.setText("Find a Solution");
            findASolutionBtn.setDisable(false);
        });
        Thread thread = new Thread(loadingTask);
        thread.start();

        Cards24Driver.getPrimaryStage().setOnCloseRequest(event -> shutdown());

        rootPane.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(10);
        fadeTransition.play();

        refresh();

        refreshBtn.setOnAction(event -> {
            refresh();
        });

        verifyBtn.setOnAction(event -> {
            try {
                if (engine.eval(expressionTextField.getText()).equals(24) && checkEquationNumbersMatch(expressionTextField.getText())) {
                    timer.cancel();
                    ButtonType playAgain = new ButtonType("Play Again", ButtonBar.ButtonData.OK_DONE);
                    ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "You were able to get the numbers you received equal to 24. \n Your completion time was " + time.getText() + "\n You solved the game in " + attempts + " attempts.",
                            playAgain,
                            exit);
                    alert.setTitle("");
                    alert.setHeaderText("Congratulations");
                    Optional<ButtonType> result = alert.showAndWait();

                    logger.log("==========Game==========");
                    logger.log("Attempts: " + attempts);
                    logger.log("Time to solve: " + time.getText());
                    logger.log("========================");

                    if (result.orElse(exit) == playAgain) {
                        refresh();
                    } else {
                        System.exit(0);
                    }

                } else {
                    attempts++;
                    System.out.println("Error");
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }

        });

        findASolutionBtn.setOnAction(event -> {
            expressionTextField.setDisable(true);
            verifyBtn.setDisable(true);
            timer.cancel();
            solutionTextField.setText("Thinking...");

            Platform.runLater(() -> {
                Thread solutionThread = new Thread(new Task() {
                    @Override
                    protected Object call() {
                        int n = 1;
                        List<String> equations = generateEquation();
                        for (String equation : equations) {
                            try {
                                if (engine.eval(equation).equals(24)) {
                                    solutionTextField.setText(equation);
                                    break;
                                }
                                if (n == equations.size()) {
                                    solutionTextField.setText("No Solution");
                                }
                                n++;
                            } catch (ScriptException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                });
                solutionThread.start();
            });
        });
    }

    /**
     * An event that when triggered will check the key typed and see if it is a digit or a operator. If it is neither it will then consume the key typed meaning it will not be added.
     * @param event The event that is used to check when a key is typed on a keyboard.
     */
    @FXML
    void onKeyTyped(KeyEvent event) {
        if (event.getEventType().equals(KeyEvent.KEY_TYPED))
            if (!Character.isDigit(event.getCharacter().charAt(0)) && !event.getCharacter().matches("[-+*\\/()]"))
                event.consume();
    }


    /**
     * A method that starts the round and refreshes it. It sets up the timer that runs the elapsed time of the game, as well as selects four random cards from the card deck and shows them visually on the scene including sound and animation.
     */
    public void refresh() {
        attempts = 0;
        time.setText("00:00");

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int seconds = 0;

            @Override
            public void run() {
                seconds++;
                time.setText(String.format("%02d", (((seconds % 86400) % 3600) / 60)) + ":" + String.format("%02d", (((seconds % 86400) % 3600) % 60)));

            }
        }, 4000, 1000);

        Random rand = new Random();

        cardPane.getChildren().clear();
        expressionTextField.clear();
        solutionTextField.clear();
        expressionTextField.setDisable(false);
        verifyBtn.setDisable(false);
        CardDeck.clearNumbersRequiredList();

        Group selectedCards = new Group();
        cardPane.getChildren().add(selectedCards);
        Timeline timeline = new Timeline();
        int cardCount = 0;
        while (cardCount < 4) {
            String[] cardFileName = cardDir[rand.nextInt(cardDir.length)].getName().split("_");
            Card card = null;
            try {
                card = new Card(cardFileName[2], cardFileName[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView selectedCard = new ImageView(card.getCardImage());
            selectedCard.setLayoutX(-200 + cardCount * 130);
            selectedCard.setLayoutY(-200);
            selectedCard.setFitWidth(90);
            selectedCard.setFitHeight(150);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(cardCount), e -> {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), selectedCard);
                translateTransition.setToX(selectedCard.getX() + 200);
                translateTransition.setToY(selectedCard.getY() + 215);
                translateTransition.play();
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), selectedCard);
                rotateTransition.setAxis(Rotate.Y_AXIS);
                rotateTransition.setFromAngle(90);
                rotateTransition.setToAngle(0);
                rotateTransition.setCycleCount(0);
                rotateTransition.play();
                MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("card.wav").toURI().toString()));
                mediaPlayer.play();
            });
            timeline.getKeyFrames().add(keyFrame);
            selectedCards.getChildren().add(selectedCard);
            cardCount++;
        }
        timeline.play();

        possibleNumberOrders.clear();
        generateNumberOrders(CardDeck.getNumbersRequired(), new Stack<>(), Card.getNumbersRequired().size());


    }

    /**
     * This checks to see if the equation includes the correct numbers that correspond to the card numbers that are currently available.
     * @param equation
     * @return
     */
    private boolean checkEquationNumbersMatch(String equation) {

        String[] numbers = equation.replace("(", "").replace(")", "").split("[-+*\\/()]");

        List<Integer> requiredNumbers = CardDeck.getNumbersRequired();

        for (int i = 0; i < numbers.length; i++) {
            if (requiredNumbers.contains(Integer.parseInt(numbers[i]))) {
                requiredNumbers.remove(new Integer(numbers[i]));
            }
        }

        if (requiredNumbers.size() == 0)
            return true;

        return false;

    }

    /**
     * This generates every possible orders the card numbers available can be in and stores them in a list.
     * @param numbers The possible card numbers that are available to use.
     * @param permutation A stack that contains the permutation of possible cards.
     * @param size The size that is required of the permutation.
     */
    private void generateNumberOrders(List<Integer> numbers, Stack<Integer> permutation, int size) {
        if (permutation.size() == size) {
            possibleNumberOrders.add(permutation.toArray(new Integer[0]));
        }
        Integer[] availableNumbers = numbers.toArray(new Integer[0]);
        for (Integer integer : availableNumbers) {
            permutation.push(integer);
            numbers.remove(integer);
            generateNumberOrders(numbers, permutation, size);
            numbers.add(permutation.pop());
        }
    }

    /**
     * This generates every possible orders that operators can be in and stores them in a list.
     * @param operators The possible operators that are available to use.
     * @param permutation A stack that contains the permutation of possible operators.
     * @param size The size that is required of the permutation.
     */
    private void generateOperatorOrders(List<String> operators, Stack<String> permutation, int size) {
        if (permutation.size() == size) {
            possibleOperatorOrders.add(permutation.toArray(new String[0]));
        }
        String[] availableOperators = operators.toArray(new String[0]);
        for (String string : availableOperators) {
            permutation.push(string);
            operators.remove(string);
            generateOperatorOrders(operators, permutation, size);
            operators.add(permutation.pop());
        }
    }

    /**
     * This generates every possible equation that the numbers and operators as well as parentheses can be in and stores them in a list.
     * @return The list of possible equations
     */
    private List<String> generateEquation() {
        String equation = "";
        List<String> equations = new ArrayList<>();
        String[] patterns = new String[]{
                "nononon",
                "(non)onon",
                "(nonon)on",
                "no(nonon)",
                "nono(non)",
                "no(non)on",
                "(non)o(non)"
        };

        for (String pattern : patterns) {
            for (Integer[] nums : possibleNumberOrders) {
                for (String[] ops : possibleOperatorOrders) {
                    int index = 0;
                    String[] pat = pattern.split("");
                    for (String p : pat) {
                        if (p.equals("n")) {
                            equation += nums[index];
                            index++;
                        } else if (p.equals("o")) {
                            equation += ops[index - 1];
                        } else {
                            if (p.equals("("))
                                equation += "(";
                            if (p.equals(")"))
                                equation += ")";
                        }
                    }
                    equations.add(equation);
                    equation = "";
                }
            }
        }

        return equations;
    }

    /**
     * This method is responsible for shutting down the application and stopping the timer to prevent memory leaking
     */
    public void shutdown() {
        timer.cancel();
        System.exit(0);
    }

}
