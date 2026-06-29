package com.employee.tests.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load config.properties", ex);
        }
    }

    private TestConfig() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String username() {
        return get("username");
    }

    public static String password() {
        return get("password");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int implicitWaitSeconds() {
        return Integer.parseInt(get("implicit.wait.seconds"));
    }
}
