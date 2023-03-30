package ua.javarush.eldidenko.hibernate_todo_app.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotNull(message = "refreshToken must not be null")
    private String refreshToken;
}
