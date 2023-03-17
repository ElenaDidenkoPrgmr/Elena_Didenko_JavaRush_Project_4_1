package ua.javarush.eldidenko.hibernate_todo_app.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(schema = "todoapp",name = "tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "title must not be null")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "registration_date")
    private Timestamp registeredAt;

    @UpdateTimestamp
    @Column(name = "last_login_date")
    private Timestamp lastUpdate;

    /*@Column(name = "user_id")
    private Long userId;*/

    @JoinColumn(name = "user_id")//, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
