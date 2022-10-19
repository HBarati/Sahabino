import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
                //TODO write in kafka
                System.out.println(line);
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
