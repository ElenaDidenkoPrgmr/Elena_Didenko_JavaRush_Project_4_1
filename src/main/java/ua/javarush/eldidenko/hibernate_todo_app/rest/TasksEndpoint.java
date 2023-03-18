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

@Path("/users/{userId}/tasks")
public class TasksEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(TasksEndpoint.class);
    private UserService userService;
    private JwtService jwtService;
    private TaskService taskService;
    @PathParam("userId") Long userId;
    @PathParam("taskId") Long taskId;
    private String token;

    @Context
    private void setRc(ResourceConfig rc, HttpHeaders httpHeaders) {
        userService = (UserService) rc.getProperty("userService");
        jwtService = (JwtService) rc.getProperty("jwtService");
        taskService = (TaskService) rc.getProperty("taskService");
        token = httpHeaders.getHeaderString("Authorization");
    }

    @GET
    @Produces("application/json")
    public Response getAllUsersTask() {
        if (isForbiddenRequest()) return Response.status(Response.Status.FORBIDDEN).build();

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
        if (isForbiddenRequest()) return Response.status(Response.Status.FORBIDDEN).build();

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
        if (isForbiddenRequest()) return Response.status(Response.Status.FORBIDDEN).build();
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
        if (isForbiddenRequest())
            return Response.status(Response.Status.FORBIDDEN).build();

        TaskDTO taskDTO = taskService.createTask(taskRequest, userId);
        return Response
                .created(URI.create("/users/" + userId + "/tasks/" + taskDTO.getId()))
                .entity(taskDTO)
                .build();
    }

    @DELETE
    @Path("{taskId}")
    public Response deleteTask() {
        if (isForbiddenRequest())
            return Response.status(Response.Status.FORBIDDEN).build();
        taskService.deleteTask(taskId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

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
