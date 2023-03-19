package ua.javarush.eldidenko.hibernate_todo_app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
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

import static ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants.*;

@Path("/users/{userId}/tasks")
public class TasksEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(TasksEndpoint.class);
    private UserService userService;
    private JwtService jwtService;
    private TaskService taskService;
    @PathParam(PATH_PARAM_USER_ID) Long userId;
    @PathParam(PATH_PARAM_TASK_ID) Long taskId;
    private String token;

    @Context
    private void setRc(ResourceConfig rc, HttpHeaders httpHeaders) {
        userService = (UserService) rc.getProperty(USER_SERVICE);
        jwtService = (JwtService) rc.getProperty(JWT_SERVICE);
        taskService = (TaskService) rc.getProperty(TASK_SERVICE);
        token = httpHeaders.getHeaderString(HEADER_TOKEN);
    }

    @GET
    @Produces("application/json")
    public Response getAllUsersTask() {
        if (isUnauthorizedRequest()) return Response.status(Response.Status.UNAUTHORIZED).build();

        List<TaskDTO> tasks = taskService.fetchTasksByUserId(userId);
        return Response
                .status(Response.Status.OK)
                .entity(tasks)
                .build();
    }

    @GET
    @Path("{taskId}")
    @Produces("application/json")
    public Response getOneTask() {
        if (isUnauthorizedRequest()) return Response.status(Response.Status.UNAUTHORIZED).build();

        TaskDTO task = taskService.fetchTasksById(taskId);
        return Response
                .status(Response.Status.OK)
                .entity(task)
                .build();
    }

    @PUT
    @Path("{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(TaskRequest taskRequest) {
        if (isUnauthorizedRequest()) return Response.status(Response.Status.UNAUTHORIZED).build();
        TaskDTO updatedTask = taskService.updateTask(taskRequest, taskId);
        return Response
                .created(URI.create("/users/" + userId + "/tasks/" + updatedTask.getId()))
                .entity(updatedTask)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(TaskRequest taskRequest) {
        if (isUnauthorizedRequest())
            return Response.status(Response.Status.UNAUTHORIZED).build();

        TaskDTO taskDTO = taskService.createTask(taskRequest, userId);
        return Response
                .created(URI.create("/users/" + userId + "/tasks/" + taskDTO.getId()))
                .entity(taskDTO)
                .build();
    }

    @DELETE
    @Path("{taskId}")
    public Response deleteTask() {
        if (isUnauthorizedRequest())
            return Response.status(Response.Status.UNAUTHORIZED).build();
        taskService.deleteTask(taskId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private boolean isUnauthorizedRequest() {
        if (!jwtService.validateAccessTokenByUserId(token, userId).isValid()) {
            return true;
        }
        if (userService.fetchUserById(userId) == null) {
            throw new WebApplicationException(USER_NOT_FOUND_MESSAGE + userId, Response.Status.NOT_FOUND);
        }
        return false;
    }
}
