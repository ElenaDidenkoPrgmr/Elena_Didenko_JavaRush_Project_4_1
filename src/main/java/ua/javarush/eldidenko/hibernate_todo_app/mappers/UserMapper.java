package ua.javarush.eldidenko.hibernate_todo_app.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ua.javarush.eldidenko.hibernate_todo_app.dto.UserDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.request.AuthenticationRequest;
import ua.javarush.eldidenko.hibernate_todo_app.request.UserRequest;

import java.util.Arrays;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INCTANCE = Mappers.getMapper(UserMapper.class);

    User requestToUser(UserRequest userRequest);

    User updateUserFromRequest(@MappingTarget User user, UserRequest userRequest);

    UserDTO userToDTO(User user);

    @Mapping(source = "password", target = "password", qualifiedByName = "passwordParser")
    User requestToUser(AuthenticationRequest authenticationRequest);

    /*@Named("passwordParser")
    static String charArrayToString(char[] password) {
        return password.toString();
    }*/
    @Named("passwordParser")
    static String charArrayToString(char[] password) {
        return Arrays.toString(password);
    }
}
