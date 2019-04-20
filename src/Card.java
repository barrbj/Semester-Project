import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Card extends CardDeck {

    private final String cardSuit;
    private final int cardValue;
    private final Image cardImage;

    public Card(String cardSuit, String cardValue) throws FileNotFoundException {
        this.cardSuit = cardSuit;
        this.cardValue = getNumberFromSuit(cardValue);
        super.addRequiredNumber(this.cardValue);
        this.cardImage = new Image(new FileInputStream("png/" + cardValue + "_of_" + cardSuit));
    }

    public String getCardSuit() {
        return cardSuit;
    }

    public int getCardValue() {
        return cardValue;
    }

    public Image getCardImage() {
        return cardImage;
    }

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

    @Override
    public String toString() {
        return "Card{" +
                "cardSuit=" + cardSuit +
                ", cardValue=" + cardValue +
                ", cardImage=" + cardImage +
                '}';
    }


}
