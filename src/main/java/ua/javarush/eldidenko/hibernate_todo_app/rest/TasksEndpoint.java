package ua.javarush.eldidenko.hibernate_todo_app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.JwtService;
import ua.javarush.eldidenko.hibernate_todo_app.services.TaskService;
import ua.javarush.eldidenko.hibernate_todo_app.services.UserService;

import java.net.URI;
import java.util.List;

@Path("/users/{userId}/tasks")
public class TasksEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(TasksEndpoint.class);
    private UserService userService;
    private JwtService jwtService;
    private TaskService taskService;
    @PathParam("userId") Long userId;
    @QueryParam("token") String token;

    @Context
    private void setRc(ResourceConfig rc) {
        userService = (UserService) rc.getProperty("userService");
        jwtService = (JwtService) rc.getProperty("jwtService");
        taskService = (TaskService) rc.getProperty("taskService");
    }


    @GET
    @Produces("application/json")
    public Response getTask() {
        if (isForbiddenRequest()) return Response.status(Response.Status.FORBIDDEN).build();


        List<TaskDTO> tasks = taskService.fetchTasksByUserId(userId);
        return Response
                .status(Response.Status.OK)
                .entity(tasks)
                .build();
    }

  /*  @PUT
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UserRequest userRequest,
                               @PathParam("userId") Long id, @QueryParam("token") String token) {
        if (validateRequest(id, token)) return Response.status(Response.Status.FORBIDDEN).build();

        UserDTO updatedUser;
        try {
            updatedUser = userService.updateUser(userRequest, id);
        } catch (UserNameIsTakenException e) {
            throw new WebApplicationException("username is taken: " + userRequest.getUsername(), Response.Status.CONFLICT);
        }
        return Response
                .created(URI.create("/users/" + id))
                .entity(updatedUser)
                .build();
    }*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(TaskRequest taskRequest) {
        if (!isForbiddenRequest()) return Response.status(Response.Status.FORBIDDEN).build();

        TaskDTO taskDTO = taskService.createTask(taskRequest, userId);
        return Response
                .created(URI.create("/users/" + userId + "/tasks/" + taskDTO.getId()))
                .entity(taskDTO)
                .build();
    }

    /*@DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") Long id,
                               @QueryParam("token") String token) {
        if (validateRequest(id, token)) return Response.status(Response.Status.FORBIDDEN).build();
        userService.deleteUser(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }*/

    private boolean isForbiddenRequest() {
        if (!jwtService.validateAccessToken(token).isValid()) {
            return true;
        }
        if (userService.fetchUserById(userId) == null) {
            throw new WebApplicationException("user not found: " + userId, Response.Status.NOT_FOUND);
        }
        return false;
    }
}
