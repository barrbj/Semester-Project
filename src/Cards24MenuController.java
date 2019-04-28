import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Cards24MenuController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView cardDisplay1;

    @FXML
    private ImageView cardDisplay2;

    @FXML
    private Button playButton;

    @FXML
    public void initialize() throws FileNotFoundException {

        File[] cardDir = new File("png").listFiles();
        Random random = new Random();

        File card1Image = cardDir[random.nextInt(cardDir.length)];
        cardDisplay1.setImage(new Image(new FileInputStream(card1Image.getAbsolutePath())));
        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(6000), cardDisplay1);
        rotateTransition1.setAxis(Rotate.Y_AXIS);
        rotateTransition1.setByAngle(360);
        rotateTransition1.setCycleCount(Animation.INDEFINITE);
        rotateTransition1.play();

        File card2Image = cardDir[random.nextInt(cardDir.length)];
        cardDisplay2.setImage(new Image(new FileInputStream(card2Image.getPath())));
        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(6000), cardDisplay2);
        rotateTransition2.setAxis(Rotate.Y_AXIS);
        rotateTransition2.setByAngle(-360);
        rotateTransition2.setCycleCount(Animation.INDEFINITE);
        rotateTransition2.play();



        playButton.setOnAction(event -> {
            FadeTransition menuFadeOutTransition = new FadeTransition(Duration.millis(2000));
            menuFadeOutTransition.setNode(rootPane);
            menuFadeOutTransition.setFromValue(10);
            menuFadeOutTransition.setToValue(0);
            menuFadeOutTransition.setOnFinished(event1 -> {
                try {
                    Cards24Driver.getPrimaryStage().setScene(new Scene(FXMLLoader.load(Cards24Driver.class.getResource("Cards24Game.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            menuFadeOutTransition.play();

        });
    }





}
