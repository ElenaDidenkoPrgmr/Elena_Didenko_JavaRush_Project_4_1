package ua.javarush.eldidenko.hibernate_todo_app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserRequest {
    @Size(min = 2, max = 25, message = "firstName Length should be between 2 and 25 character")
    @NotNull(message = "username must not be null")
    private String username;

    @Size(min = 8, max = 25, message = "password Length should be between 8 and 25 character")
    @NotNull(message = "password must not be null")
    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    @Email(message = "incorrect email")
    private String email;
}
