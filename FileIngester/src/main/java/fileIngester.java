import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileIngester {

    public static void main(String args[]) throws IOException, InterruptedException {
        while (true){
            ConfigReader config = ConfigReader.load();
            File directoryPath = new File(config.getFolderName());
            File[] filesList = directoryPath.listFiles();
            System.out.println("List of files and directories in the specified directory:");
            List<Thread> threadList = new ArrayList<>();
            if (filesList != null) {
                for(File file : filesList) {
                    //TODO multithread refactor (ThreadPool)
                    Thread thread = new Thread(new LogIngester(file));
                    threadList.add(thread);
                }
            }
            for (Thread thread : threadList) {
                thread.start();
            }
            break;
        }
    }
}