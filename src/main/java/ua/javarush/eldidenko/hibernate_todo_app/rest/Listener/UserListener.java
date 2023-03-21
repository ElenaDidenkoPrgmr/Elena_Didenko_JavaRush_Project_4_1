package ua.javarush.eldidenko.hibernate_todo_app.rest.Listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;

public class UserListener {
    private static final Logger LOGGER = LogManager.getLogger(UserListener.class);

    @PrePersist
    @PreUpdate
    private void loggingUpdateUser(User user) {
        if (user.getId() == null) {
            LOGGER.debug("[USER AUDIT] user add, username:" + user.getUsername());
        } else {
            LOGGER.debug("[USER AUDIT] user update, username:" + user.getUsername());
        }
    }

    @PreRemove
    private void loggingDeleteUser(User user) {
        LOGGER.debug("[USER AUDIT] user delete, username:" + user.getUsername());
    }
}
