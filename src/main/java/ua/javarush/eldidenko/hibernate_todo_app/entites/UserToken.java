package ua.javarush.eldidenko.hibernate_todo_app.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "todoapp",name = "user_tokens")

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserToken {
    @Id
    @Column(name="user_id")
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
