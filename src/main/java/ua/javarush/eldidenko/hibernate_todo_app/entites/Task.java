package ua.javarush.eldidenko.hibernate_todo_app.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants;

import java.sql.Timestamp;

@Entity
@Table(schema = "todoapp",name = "tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = AppConstants.VALIDATE_TASK_TITLE_MESSAGE)
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "last_update")
    private Timestamp lastUpdate;
}
