import config.ConfigReader;
import ingester.LogIngester;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    /**
     * In the Main method, first read the names of logs folder from fileConfig,
     * second is any file is exist on the folder, read them and made file list from them,
     * third read each one of log file in one different thread,
     * And it always repeats the second and third steps to check if new log files are added.
     */
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        Logger logger = Logger.getLogger(LogIngester.class);
        ConfigReader config = ConfigReader.load();
        File directoryPath = new File(config.getFolderName());
        logger.info("read log files from folder with name: " + config.getFolderName());
        while (true) {
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