package ua.javarush.eldidenko.hibernate_todo_app.services.token_entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Tokens {
    private String accessToken;
    private String refreshToken;
}
