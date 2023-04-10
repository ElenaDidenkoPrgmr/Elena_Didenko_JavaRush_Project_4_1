package ua.javarush.eldidenko.hibernate_todo_app.services.token_entity;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidation {
    private boolean isValid;
}
