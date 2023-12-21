package org.packman.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.packman.server.socket.ServerSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final String CONFIG_FILE = "application.yml";
    private static final Logger logger = LogManager.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        properties = new Properties();
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    if (value != null && value.startsWith("${") && value.endsWith("}")) {
                        String envVar = value.substring(2, value.length() - 1);
                        String envVarValue = System.getenv(envVar);
                        if (envVarValue != null) {
                            properties.setProperty(key, envVarValue);
                        }
                    }
                }
            } else {
                logger.error("Unable to find {}", CONFIG_FILE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getPort() {
        return Integer.valueOf(properties.getProperty("PORT"));
    }

}
