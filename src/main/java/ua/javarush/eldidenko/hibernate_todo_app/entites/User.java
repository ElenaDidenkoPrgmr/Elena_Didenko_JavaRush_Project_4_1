package ua.javarush.eldidenko.hibernate_todo_app.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.javarush.eldidenko.hibernate_todo_app.rest.Listener.UserListener;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(schema = "todoapp",name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(UserListener.class)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "username must not be null")
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull(message = "password must not be null")
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Email(message = "incorrect email")
    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "registration_date")
    private Timestamp registeredAt;

    @UpdateTimestamp
    @Column(name = "last_login_date")
    private Timestamp lastUpdate;

    @OneToOne(mappedBy="user", cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private UserToken userToken;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Task> tasks;

}
