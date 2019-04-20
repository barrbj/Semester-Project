import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    private static List<Integer> numbersRequired = new ArrayList<>();

    public void addRequiredNumber(Integer num){
        numbersRequired.add(num);
    }

    public static List<Integer> getNumbersRequired() {
        return numbersRequired;
    }

    public static void clearNumbersRequiredList(){
        numbersRequired.clear();
    }
}
