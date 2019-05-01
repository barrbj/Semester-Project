import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Card extends CardDeck {

    private final String cardSuit;
    private final int cardValue;
    private final Image cardImage;

    /**
     *  This is the card constructor method
     * @param cardSuit The suit of the card
     * @param cardValue The value of the card
     * @throws FileNotFoundException 
     */
    public Card(String cardSuit, String cardValue) throws FileNotFoundException {
        this.cardSuit = cardSuit;
        this.cardValue = getNumberFromSuit(cardValue);
        super.addRequiredNumber(this.cardValue);
        this.cardImage = new Image(new FileInputStream("png/" + cardValue + "_of_" + cardSuit));
    }

    /**
     *  Gets the suit of the card
     * @return A string containing the suit of the card
     */
    public String getCardSuit() {
        return cardSuit;
    }

    /**
     *  Gets the value of the card
     * @return The value of a card as an integer
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     *  Gets the image of the card
     * @return The image of the card
     */
    public Image getCardImage() {
        return cardImage;
    }

    /**
     *  Gets the number from the suit of the card
     * @param cardValue The string value of the card
     * @return The integer value of the card
     */
    private int getNumberFromSuit(String cardValue) {
        switch (cardValue) {
            case "ace":
                return 1;
            case "jack":
                return 11;
            case "queen":
                return 12;
            case "king":
                return 13;
            default:
                return Integer.parseInt(cardValue);

        }
    }
    
    /**
     * Overriding of the toString method to show the variables of a card
     * @return The string value of the card object
     */
    @Override
    public String toString() {
        return "Card{" +
                "cardSuit=" + cardSuit +
                ", cardValue=" + cardValue +
                ", cardImage=" + cardImage +
                '}';
    }


}
