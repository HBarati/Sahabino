package ingester;

import entity.LogModel;
import kafkaFactory.KafkaLogsProducer;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In this class give the log file from Main and parse them to logModel frame and write to the kafka.
 */
public class LogIngester implements Runnable {
    private File file;
    Logger logger = Logger.getLogger(LogIngester.class);

    public LogIngester(File file) {
        this.file = file;
    }
    /**
     * This method give the one file and check each of line from the log file is match with log4j
     * format, write on the kafka as producer.
     */
    @Override
    public void run() {
        logger.info("in Thread: " + Thread.currentThread().getName() + " write log file: " + file.getName() + " in kafka");
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            LogModel log;
            while ((line = br.readLine()) != null) {
                String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.*)\\] ([^ ]*) +([^ ]*) - (.*)$";

                Pattern p = Pattern.compile(regex);
                Matcher matcher = p.matcher(line);
                if (matcher.matches() && matcher.groupCount() == 6) {
                    log = new LogModel(matcher.group(1), matcher.group(2), matcher.group(3),
                            matcher.group(4), matcher.group(5), matcher.group(6));
                    KafkaLogsProducer.runProducer(log);
                }
                else {
                    logger.error("The logs of "+file.getName()+" file is not valid!");
                }
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
