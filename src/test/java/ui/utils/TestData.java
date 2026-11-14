package ui.utils;

import java.io.InputStream;
import java.util.Properties;

public class TestData {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream inputStream = TestData.class.getClassLoader()
                .getResourceAsStream("test-data.properties")) {
            if (inputStream != null) {
                PROPS.load(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить selenide.properties", e);
        }
    }

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Ключ не найден в properties: " + key);
        }
        return value;
    }
}
