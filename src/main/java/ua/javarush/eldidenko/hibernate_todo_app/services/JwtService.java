package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.request.RefreshTokenRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.TokenValidation;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.Tokens;

public interface JwtService {
    Tokens generateAndSaveTokens(Long userId);

    //Tokens generateAndSaveTokens(User userId);

    TokenValidation validateAccessToken(String token);


    Tokens refreshTokens(RefreshTokenRequest refreshToken);
}
