import config.ConfigReader;
import ingester.LogIngester;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        Logger logger = Logger.getLogger(LogIngester.class);
        ConfigReader config = ConfigReader.load();
        while (true) {
            logger.info("read log files from folder with name: " + config.getFolderName());
            File directoryPath = new File(config.getFolderName());
            File[] filesList = directoryPath.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    Thread thread = new Thread(new LogIngester(file));
                    logger.info("file: " + file.getName() + " is read");
                    executor.execute(thread);
                }
            }
            Thread.sleep(Long.parseLong(config.getLogProducingIntervalMilliSecond()));
        }
    }
}