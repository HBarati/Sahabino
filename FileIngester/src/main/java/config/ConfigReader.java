package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader class make configurable our parameters and read them from fileConfig.properties
 * and give them with getter class
 */
public class ConfigReader {
    private static ConfigReader instance;
    private String folderName;
    private String Topic;
    private String BootStrapServer;
    private String logProducingIntervalMilliSecond;


    public static ConfigReader load() {
        if (instance == null) {
            instance = new ConfigReader();
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("fileConfig.properties");
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            instance.folderName = properties.getProperty("log.fileName");
            instance.Topic = properties.getProperty("Topic");
            instance.BootStrapServer = properties.getProperty("BootStrapServer");
            instance.logProducingIntervalMilliSecond = properties.getProperty("logProducingIntervalMilliSecond");

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

    public String getLogProducingIntervalMilliSecond() {
        return logProducingIntervalMilliSecond;
    }
}

