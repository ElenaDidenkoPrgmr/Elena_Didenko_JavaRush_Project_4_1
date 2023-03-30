package ua.javarush.eldidenko.hibernate_todo_app.Listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;

public class UserTokenListener {
    private static final Logger LOGGER = LogManager.getLogger(UserTokenListener.class);
    public static final String LOGGER_MESSAGE_ADD_USER_TOKEN = "[USER_TOKEN AUDIT] userToken add, userId: ";
    public static final String LOGGER_MESSAGE_UPDATE_USER_TOKEN = "[USER_TOKEN AUDIT] userToken update, username: ";

    @PrePersist
    private void loggingCreateUserToken(UserToken userToken) {
            LOGGER.debug( LOGGER_MESSAGE_ADD_USER_TOKEN + userToken.getUser());
    }

    @PreUpdate
    private void loggingUpdateUserToken(UserToken userToken) {
        LOGGER.debug( LOGGER_MESSAGE_UPDATE_USER_TOKEN + userToken.getUser());
    }
}
