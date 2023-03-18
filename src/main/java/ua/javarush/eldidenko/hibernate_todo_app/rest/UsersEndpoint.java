package ua.javarush.eldidenko.hibernate_todo_app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import ua.javarush.eldidenko.hibernate_todo_app.dto.UserDTO;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserNameIsTakenException;
import ua.javarush.eldidenko.hibernate_todo_app.request.UserRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.JwtService;
import ua.javarush.eldidenko.hibernate_todo_app.services.UserService;

import java.net.URI;

@Path("/users")
public class UsersEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(UsersEndpoint.class);
    private UserService userService;
    private JwtService jwtService;
    private @PathParam("userId") Long userId;
    private String token;

    @Context
    private void setRc(ResourceConfig rc, HttpHeaders httpHeaders) {
        userService = (UserService) rc.getProperty("userService");
        jwtService = (JwtService) rc.getProperty("jwtService");
        token = httpHeaders.getHeaderString("Authorization");
    }


    @GET
    @Path("{userId}")
    @Produces("application/json")
    public Response getUser() {
        UserDTO userDTO = userService.fetchUserById(userId);
        if (userDTO == null) {
            throw new WebApplicationException("user not found: " + userId, Response.Status.NOT_FOUND);
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
            throw new WebApplicationException("username is taken: " + userRequest.getUsername(), Response.Status.CONFLICT);
        }
        return Response
                .created(URI.create("/users/" + userId))
                .entity(updatedUser)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserRequest userRequest) {

        UserDTO userDTO;
        try {
            userDTO = userService.createUser(userRequest);
        } catch (UserNameIsTakenException e) {
            throw new WebApplicationException("username is taken: " + userRequest.getUsername(), Response.Status.CONFLICT);
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
            throw new WebApplicationException("user not found: " + userId, Response.Status.NOT_FOUND);
        }
        return false;
    }
}
