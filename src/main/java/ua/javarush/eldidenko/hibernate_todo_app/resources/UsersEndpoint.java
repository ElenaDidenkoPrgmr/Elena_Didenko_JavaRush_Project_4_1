package ua.javarush.eldidenko.hibernate_todo_app.resources;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.glassfish.jersey.server.ResourceConfig;
import ua.javarush.eldidenko.hibernate_todo_app.dto.UserDTO;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserNameIsTakenException;
import ua.javarush.eldidenko.hibernate_todo_app.request.UserRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.JwtService;
import ua.javarush.eldidenko.hibernate_todo_app.services.UserService;

import java.net.URI;

import static ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants.*;

@Path("/users")
public class UsersEndpoint {
    private static final String USERNAME_TAKEN_MESSAGE = "username is taken: ";
    private UserService userService;
    private JwtService jwtService;
    private @PathParam(PATH_PARAM_USER_ID) Long userId;
    private String token;

    @Context
    private void setRc(ResourceConfig rc, HttpHeaders httpHeaders) {
        userService = (UserService) rc.getProperty(USER_SERVICE);
        jwtService = (JwtService) rc.getProperty(JWT_SERVICE);
        token = httpHeaders.getHeaderString(HEADER_TOKEN);
    }


    @GET
    @Path("{userId}")
    @Produces("application/json")
    public Response getUser() {
        UserDTO userDTO = userService.fetchUserById(userId);
        if (userDTO == null) {
            throw new WebApplicationException(USER_NOT_FOUND_MESSAGE + userId, Response.Status.NOT_FOUND);
        }
        return Response
                .status(Response.Status.OK)
                .entity(userDTO)
                .build();
    }

    @PUT
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UserRequest userRequest) {
        if (isUnauthorizedRequest()) return Response.status(Response.Status.UNAUTHORIZED).build();

        UserDTO updatedUser;
        try {
            updatedUser = userService.updateUser(userRequest, userId);
        } catch (UserNameIsTakenException e) {
            throw new WebApplicationException(USERNAME_TAKEN_MESSAGE + userRequest.getUsername(), Response.Status.CONFLICT);
        }
        return Response
                .created(URI.create("/users/" + userId))
                .entity(updatedUser)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserRequest userRequest) {

        UserDTO userDTO;
        try {
            userDTO = userService.createUser(userRequest);
        } catch (UserNameIsTakenException e) {
            throw new WebApplicationException(USERNAME_TAKEN_MESSAGE + userRequest.getUsername(), Response.Status.CONFLICT);
        }
        return Response
                .created(URI.create("/users/" + userDTO.getId()))
                .entity(userDTO)
                .build();
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser() {
        if (isUnauthorizedRequest()) return Response.status(Response.Status.UNAUTHORIZED).build();
        userService.deleteUser(userId);
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
