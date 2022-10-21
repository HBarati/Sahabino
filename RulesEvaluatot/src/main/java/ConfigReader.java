import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private String Type1LogName;

    public static ConfigReader load() throws IOException {
        if (instance == null) {
            instance = new ConfigReader();
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("rulesType.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            }
            instance.Type1LogName = properties.getProperty("Type1LogName");
        }
        return instance;
    }

    public String getType1LogName() {
        return Type1LogName;
    }
}

