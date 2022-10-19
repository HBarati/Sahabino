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
        logger.info(Thread.currentThread().getName());
        //TODO LOGGING
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
            System.out.println("");
            //TODO LOGGING
        }
    }
}
