package ua.javarush.eldidenko.hibernate_todo_app.exceptions;

public class WrongSettingsDBException extends RuntimeException {
    public WrongSettingsDBException() {
    }

    public WrongSettingsDBException(String message) {
        super(message);
    }

    public WrongSettingsDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
