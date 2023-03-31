package ua.javarush.eldidenko.hibernate_todo_app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserRequest {
    @Size(min = 2, max = 25, message = AppConstants.VALIDATE_USERNAME_LENGTH_MESSAGE)
    @NotNull(message = AppConstants.VALIDATE_USERNAME_NOT_NULL_MESSAGE)
    private String username;

    @Size(min = 8, max = 25, message = AppConstants.VALIDATE_PASSWORD_LENGTH_MESSAGE)
    @NotNull(message = AppConstants.VALIDATE_PASSWORD_NOT_NULL_MESSAGE)
    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    @Email(message = AppConstants.VALIDATE_EMAIL_MESSAGE)
    private String email;
}
