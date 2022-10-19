import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class fileIngester {

    public static void main(String args[]) throws IOException, InterruptedException {
        while (true){
            ConfigReader config = ConfigReader.load();
            File directoryPath = new File(config.getFolderName());
            File[] filesList = directoryPath.listFiles();
            System.out.println("List of files and directories in the specified directory:");
//            List<Thread> threadList = new ArrayList<>();
            if (filesList != null) {
                for(File file : filesList) {
                    Thread thread = new Thread(new LogIngester(file));
                    Executor executor = Executors.newCachedThreadPool();
                    executor.execute(thread);
                }
            }
//            for (Thread thread : threadList) {
//            }
            break;
        }
    }
}