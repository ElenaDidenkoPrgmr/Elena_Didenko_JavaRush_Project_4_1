package ua.javarush.eldidenko.hibernate_todo_app.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    TaskMapper INCTANCE = Mappers.getMapper(TaskMapper.class);

    Task requestToTask(TaskRequest taskRequest);

    TaskDTO taskToDTO(Task task);

    Task updateTaskFromRequest(@MappingTarget Task task, TaskRequest taskRequest);
}
