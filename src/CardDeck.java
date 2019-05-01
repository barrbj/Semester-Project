import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    private static List<Integer> numbersRequired = new ArrayList<>();

    
    /**
     *  Adds the required number 
     * @param num The integer value of the card
     */
    public void addRequiredNumber(Integer num){
        numbersRequired.add(num);
    }

    /**
     *  Gets the required numbers
     * @return The list of the required numbers
     */
    public static List<Integer> getNumbersRequired() {
        return numbersRequired;
    }

    /**
     *  Clears the list of numbers
     */
    public static void clearNumbersRequiredList(){
        numbersRequired.clear();
    }
}
