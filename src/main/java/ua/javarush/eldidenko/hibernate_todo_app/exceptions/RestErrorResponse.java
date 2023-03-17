package ua.javarush.eldidenko.hibernate_todo_app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestErrorResponse {
    private Object subject;
    private String message;
}
