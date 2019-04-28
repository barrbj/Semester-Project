import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Logger {

    private File file;

    public Logger() {
        this.file = new File("log.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String string){
        try {
            BufferedWriter bufferedWriter  = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append("[" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + "] " + string + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
