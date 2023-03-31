package ua.javarush.eldidenko.hibernate_todo_app.constants;

public class AppConstants {
    public static final String SESSION_FACTORY = "sessionFactory";
    public static final String USER_SERVICE = "userService";
    public static final String JWT_SERVICE = "jwtService";
    public static final String TASK_SERVICE = "taskService";
    public static final String HEADER_TOKEN = "Authorization";

    public static final String PATH_PARAM_USER_ID = "userId";
    public static final String PATH_PARAM_TASK_ID = "taskId";

    public static final String USER_NOT_FOUND_MESSAGE = "user not found: ";

    public static final String VALIDATE_USERNAME_NOT_NULL_MESSAGE = "username must not be null";
    public static final String VALIDATE_USERNAME_LENGTH_MESSAGE = "username Length should be between 2 and 25 character";
    public static final String VALIDATE_PASSWORD_NOT_NULL_MESSAGE = "password must not be null";
    public static final String VALIDATE_PASSWORD_LENGTH_MESSAGE = "password Length should be between 8 and 25 character";
    public static final String VALIDATE_EMAIL_MESSAGE = "incorrect email";
    public static final String VALIDATE_TASK_TITLE_MESSAGE = "title must not be null";
    public static final String VALIDATE_REFRESH_TKN_MESSAGE = "refreshToken must not be null";
}
