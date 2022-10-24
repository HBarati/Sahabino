package ingester;

import entity.LogModel;
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
                    String[] logPart = new String[6];
                    for (int i = 0; i < 6; i++) {
                        logPart[i] = matcher.group(i + 1);
                    }
                    LogModel log = new LogModel(logPart[0], logPart[1], logPart[2], logPart[3], logPart[4], logPart[5]);

                    KafkaLogsProducer kafkaLogsProducer = new KafkaLogsProducer();
                    kafkaLogsProducer.runProducer(log);

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
