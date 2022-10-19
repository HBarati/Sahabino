import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIngester implements Runnable{
    private File file;
    final static Logger logger = Logger.getLogger(LogIngester.class);

    public LogIngester(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        logger.info("in Thread: "+Thread.currentThread().getName()+" write log file: "+file.getName()+" in kafka");
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.*)\\] ([^ ]*) +([^ ]*) - (.*)$";

                Pattern p = Pattern.compile(regex);
                String sample = line;
                Matcher m = p.matcher(sample);
                System.out.println(m.matches());
                if (m.matches() && m.groupCount() == 6) {
                    LocalDate date = LocalDate.parse(m.group(1));
//                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
                    String time = m.group(2);
                    String threadId = m.group(3);
                    String priority = m.group(4);
                    String category = m.group(5);
                    String message = m.group(6);

                    System.out.println("date: " + date);
                    System.out.println("time: " + time);
                    System.out.println("threadId: " + threadId);
                    System.out.println("priority: " + priority);
                    System.out.println("category: " + category);
                    System.out.println("message: " + message);
                    System.out.println();
                }
                //TODO write in kafka
//                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean delete = file.delete();
        if (delete) {
            logger.info(file.getName()+" is deleted");
        }
        else {
            logger.error(file.getName()+" cant  be deleted!");
        }
    }
}
