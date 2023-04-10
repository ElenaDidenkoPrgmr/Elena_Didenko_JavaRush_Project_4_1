package ua.javarush.eldidenko.hibernate_todo_app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants;

@Data

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@Getter
@Setter

public class AuthenticationRequest {
    @NotNull(message = AppConstants.VALIDATE_USERNAME_NOT_NULL_MESSAGE)
    private String username;

    @NotNull(message = AppConstants.VALIDATE_PASSWORD_NOT_NULL_MESSAGE)
    private char[] password;
}
