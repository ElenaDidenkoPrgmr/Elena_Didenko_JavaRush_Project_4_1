package ua.javarush.eldidenko.hibernate_todo_app.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotNull(message = AppConstants.VALIDATE_REFRESH_TKN_MESSAGE)
    private String refreshToken;
}
