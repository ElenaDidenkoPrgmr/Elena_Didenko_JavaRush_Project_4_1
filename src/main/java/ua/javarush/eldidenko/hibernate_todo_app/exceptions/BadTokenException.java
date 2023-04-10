package ua.javarush.eldidenko.hibernate_todo_app.exceptions;

public class BadTokenException extends RuntimeException{
    public BadTokenException(String message) {
        super(message);
    }

    public BadTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
