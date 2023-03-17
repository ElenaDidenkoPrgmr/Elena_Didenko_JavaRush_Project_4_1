package ua.javarush.eldidenko.hibernate_todo_app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}
