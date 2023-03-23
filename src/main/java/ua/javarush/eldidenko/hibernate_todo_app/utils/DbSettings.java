package ua.javarush.eldidenko.hibernate_todo_app.utils;

import ua.javarush.eldidenko.hibernate_todo_app.exceptions.WrongSettingsDBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class DbSettings {
    private final String ERROR_FILE = "Error during file reading; path: ";
    private final String ERROR_SETTINGS = "Error during load settings from file. ";

    public Properties dbProperties() {
        File appPropertiesFile = new File("db.properties");
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(appPropertiesFile);
        } catch (FileNotFoundException ex) {
            throw new WrongSettingsDBException(ERROR_FILE + appPropertiesFile + ex.getMessage(), ex);
        }

        Properties propertiesFromFile = new Properties();
        try {
            propertiesFromFile.load(fileReader);
        } catch (IOException ex) {
            throw new WrongSettingsDBException(ERROR_SETTINGS + appPropertiesFile + ex.getMessage(), ex);
        }

        return propertiesFromFile;
    }

}
