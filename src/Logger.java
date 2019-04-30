import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    /**
     * The file in which events are logged.
     */
    private File file;

    /**
     * Initialization of the logger class. This is where the file is assigned to the file object as well as if the file doesn't exist it will be created.
     */
    public Logger() {
        this.file = new File("log.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method of logging a string of information into a log file that also contains the timestamp of when the log was made.
     * @param string The data which is to appear in the log file.
     */
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
