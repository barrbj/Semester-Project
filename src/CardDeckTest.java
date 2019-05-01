import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CardDeckTest {

    @Test
    public void checkDeckAdd(){
        CardDeck.getNumbersRequired().addAll(Arrays.asList(4, 2, 13, 10));
    }

    @Test
    public void checkList(){
        CardDeck.getNumbersRequired().addAll(Arrays.asList(4, 2, 13, 10));
        assertEquals(CardDeck.getNumbersRequired().size(), 4);
    }

    @Test
    public void checkClearList(){
        CardDeck.clearNumbersRequiredList();
        assertEquals(CardDeck.getNumbersRequired().size(), 0);
    }

}