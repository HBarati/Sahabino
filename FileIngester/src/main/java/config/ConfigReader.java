package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private String folderName;
    private String Topic;
    private String BootStrapServer;

    public static ConfigReader load() throws IOException {
        if (instance == null) {
            instance = new ConfigReader();
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("logfile.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            }
            instance.folderName = properties.getProperty("log.fileName");
            instance.Topic = properties.getProperty("Topic");
            instance.BootStrapServer = properties.getProperty("BootStrapServer");

        }
        return instance;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getTopic() {
        return Topic;
    }

    public String getBootStrapServer() {
        return BootStrapServer;
    }
}

