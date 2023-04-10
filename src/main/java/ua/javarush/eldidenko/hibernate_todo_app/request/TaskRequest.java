package ua.javarush.eldidenko.hibernate_todo_app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskRequest {

    @NotNull(message = AppConstants.VALIDATE_TASK_TITLE_MESSAGE)
    private String title;

    private String description;
}
