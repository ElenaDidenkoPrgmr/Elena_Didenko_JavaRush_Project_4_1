package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;

public interface TokenRepository {
    void save(UserToken userToken, Long userId);

    String fetchAccessTokenByUserId(Long userId);

    UserToken fetchByRefreshToken(String refreshToken);
}
