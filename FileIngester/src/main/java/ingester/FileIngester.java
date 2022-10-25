package ingester;

import config.ConfigReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileIngester {
    public static void main(String[] args) throws IOException, InterruptedException {
        while (true){
            Logger logger = Logger.getLogger(LogIngester.class);
            ConfigReader config = ConfigReader.load();
            logger.info("read log files from folder with name: "+config.getFolderName());
            File directoryPath = new File(config.getFolderName());
            File[] filesList = directoryPath.listFiles();
            Date date = new Date();
            System.out.println("List of files and directories in the specified directory:"+date.getTime());
            if (filesList != null) {
                Executor executor = Executors.newCachedThreadPool();
                for(File file : filesList) {
                    Thread thread = new Thread(new LogIngester(file));
                    logger.info("file: "+file.getName()+" is read");
                    executor.execute(thread);
                }
            }
            Thread.sleep(1500);
        }
    }
}