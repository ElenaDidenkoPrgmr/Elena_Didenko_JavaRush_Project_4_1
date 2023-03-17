package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.dto.UserDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserNameIsTakenException;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserUnauthorizedException;
import ua.javarush.eldidenko.hibernate_todo_app.request.UserRequest;

public interface UserService {

    UserDTO createUser(UserRequest userRequest) throws UserNameIsTakenException;

    UserDTO fetchUserById(Long userId);

    UserDTO updateUser(UserRequest userRequest, Long id)  throws UserNameIsTakenException;

    void deleteUser(Long id);

    void hashPassword(UserRequest userRequest);

    User fetchByUserName(String username);

    Long authenticateUser(String username, char[] password) throws UserUnauthorizedException;
}
