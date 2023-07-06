package Utils;

import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private Properties props;

    private AppConfig() {
        try {
            props = new Properties();
            props.load(getClass().getResourceAsStream("../resources/config.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static AppConfig get() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getAppName() {
        return props.getProperty("appname", "FPMS");
    }

    public String getVersion() {
        return props.getProperty("version", "1.0,0");
    }

    public String getReleased() {
        return props.getProperty("released", "2022-05-25");
    }

    public String getConnectionString() {
        return props.getProperty("connectionString");
    }

    public String getDriverType() {
        return props.getProperty("driverType");
    }
}

