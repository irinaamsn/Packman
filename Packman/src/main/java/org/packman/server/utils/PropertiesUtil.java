package org.packman.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final String CONFIG_FILE = "application.yml";

    private static Properties properties;

    static {
        properties = new Properties();
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getPort() {
        return Integer.valueOf(properties.getProperty("PORT"));
    }

}
