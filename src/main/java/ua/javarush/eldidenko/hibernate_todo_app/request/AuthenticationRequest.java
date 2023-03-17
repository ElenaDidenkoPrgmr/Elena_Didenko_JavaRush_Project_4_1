package ua.javarush.eldidenko.hibernate_todo_app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@Getter
@Setter

public class AuthenticationRequest {
    @NotNull(message = "username must not be null")
    private String username;

    @NotNull(message = "password must not be null")
    private char[] password;
}
