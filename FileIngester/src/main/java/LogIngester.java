import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIngester implements Runnable {
    private File file;
    Logger logger = Logger.getLogger(LogIngester.class);

    public LogIngester(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        logger.info("in Thread: " + Thread.currentThread().getName() + " write log file: " + file.getName() + " in kafka");
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.*)\\] ([^ ]*) +([^ ]*) - (.*)$";

                Pattern p = Pattern.compile(regex);
                String sample = line;
                Matcher matcher = p.matcher(sample);
                System.out.println(matcher.matches());
                if (matcher.matches() && matcher.groupCount() == 6) {
                    //TODO date and time fix
                    String date = matcher.group(1);
//                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
                    String time = matcher.group(2);
                    String threadId = matcher.group(3);
                    String priority = matcher.group(4);
                    String category = matcher.group(5);
                    String message = matcher.group(6);
                    LogModel log = new LogModel(date, time, threadId, priority, category, message);

                    KafkaLogsProducer kafkaLogsProducer = new KafkaLogsProducer();
                    kafkaLogsProducer.runProducer(log);

                }
                //TODO write in kafka
//                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean delete = file.delete();
        if (delete) {
            logger.info(file.getName() + " is deleted");
        } else {
            logger.error(file.getName() + " cant  be deleted!");
        }
    }
}
