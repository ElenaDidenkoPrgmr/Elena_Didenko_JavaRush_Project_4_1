package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import ua.javarush.eldidenko.hibernate_todo_app.entites.User;

public interface UserRepository {
    User fetchById(Long id);

    User fetchByUserName(String name);

    User save(User user);

    User updateUser(User update);

    void deleteUser(Long id);
}
