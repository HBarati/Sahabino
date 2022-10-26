package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * ConfigReader class make configurable our parameters and read them from rulesConfig.properties
 * and give them with getter class
 */
public class ConfigReader {
    private static ConfigReader instance;

    private String Topic;
    private String BootStrapServer;
    private String logDateFormat;
    private String ruleEvaluatingInterval;

    private String DBUrl;
    private String DBUser;
    private String DBPassword;

    private String Type1LogPriority;

    private String Type2LogPriority;
    private String Type2LogCategory;
    private String Type2Period;
    private String Type2Rate;

    private String Type3LogCategory;
    private String Type3Period;
    private String Type3Rate;

    public static ConfigReader load(){
        if (instance == null) {
            instance = new ConfigReader();
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("rulesConfig.properties");
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //all

            instance.logDateFormat = properties.getProperty("logDateFormat");
            instance.ruleEvaluatingInterval = properties.getProperty("ruleEvaluatingIntervalMilliSecond");
            //kafkaConsumer
            instance.Topic = properties.getProperty("Topic");
            instance.BootStrapServer = properties.getProperty("BootStrapServer");
            //DB information
            instance.DBUrl = properties.getProperty("DBUrl");
            instance.DBUser = properties.getProperty("DBUser");
            instance.DBPassword = properties.getProperty("DBPassword");
            //Type1
            instance.Type1LogPriority = properties.getProperty("Type1LogPriority");
            //Type2
            instance.Type2LogPriority = properties.getProperty("Type2LogPriority");
            instance.Type2LogCategory = properties.getProperty("Type2LogCategory");
            instance.Type2Period = properties.getProperty("Type2Period");
            instance.Type2Rate = properties.getProperty("Type2Rate");
            //Type3
            instance.Type3LogCategory = properties.getProperty("Type3LogCategory");
            instance.Type3Period = properties.getProperty("Type3Period");
            instance.Type3Rate = properties.getProperty("Type3Rate");
        }
        return instance;
    }

    public String getDBUrl() {
        return DBUrl;
    }

    public String getDBUser() {
        return DBUser;
    }

    public String getDBPassword() {
        return DBPassword;
    }

    public String getType1LogPriority() {
        return Type1LogPriority;
    }

    public String getType2LogPriority() {
        return Type2LogPriority;
    }

    public String getType2LogCategory() {
        return Type2LogCategory;
    }

    public String getType2Period() {
        return Type2Period;
    }

    public String getType2Rate() {
        return Type2Rate;
    }

    public String getType3LogCategory() {
        return Type3LogCategory;
    }

    public String getType3Period() {
        return Type3Period;
    }

    public String getType3Rate() {
        return Type3Rate;
    }

    public String getBootStrapServer() {
        return BootStrapServer;
    }

    public String getTopic() {
        return Topic;
    }
    public String getLogDateFormat() {
        return logDateFormat;
    }


    public String getRuleEvaluatingInterval() {
        return ruleEvaluatingInterval;
    }
}

