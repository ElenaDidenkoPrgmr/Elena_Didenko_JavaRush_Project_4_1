package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.request.RefreshTokenRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.TokenValidation;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.Tokens;

public interface JwtService {
    Tokens generateAndSaveTokens(Long userId);

    TokenValidation validateAccessTokenByUserId(String token, Long userId);

    Tokens refreshTokens(RefreshTokenRequest refreshToken);
}
