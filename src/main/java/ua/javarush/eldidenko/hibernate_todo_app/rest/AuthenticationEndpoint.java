package ua.javarush.eldidenko.hibernate_todo_app.rest;

import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.BadTokenException;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserUnauthorizedException;
import ua.javarush.eldidenko.hibernate_todo_app.request.AuthenticationRequest;
import ua.javarush.eldidenko.hibernate_todo_app.request.RefreshTokenRequest;
import ua.javarush.eldidenko.hibernate_todo_app.rest.Listener.UserTokenListener;
import ua.javarush.eldidenko.hibernate_todo_app.services.JwtService;
import ua.javarush.eldidenko.hibernate_todo_app.services.UserService;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.TokenValidation;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.Tokens;

import java.net.http.HttpRequest;

import static ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants.JWT_SERVICE;
import static ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants.USER_SERVICE;

@Path("/authentication")
public class AuthenticationEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationEndpoint.class);
    public static final String LOGGER_MESSAGE_UNSUCCESS_VALIDATE_TOKEN = "[USER_TOKEN AUDIT] wrong token: userId = {}, token = {} ";
    public static final String LOGGER_MESSAGE_UNSUCCESS_LOGIN = "[USER_AUTH AUDIT] wrong credential: username = {}";

    private UserService userService;
    private JwtService jwtService;

    @Context
    private void setRc(ResourceConfig rc) {
        userService = (UserService) rc.getProperty(USER_SERVICE);
        jwtService = (JwtService) rc.getProperty(JWT_SERVICE);
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(@QueryParam("token") String token, @PathParam("userId") Long userId) {
        if (token == null || token.isBlank()) {
            LOGGER.warn(LOGGER_MESSAGE_UNSUCCESS_VALIDATE_TOKEN, userId,token);
            throw new WebApplicationException("token is empty ", Response.Status.UNAUTHORIZED);
        }
        TokenValidation tokenValidation = jwtService.validateAccessTokenByUserId(token, userId);

        if (!tokenValidation.isValid()) LOGGER.warn(LOGGER_MESSAGE_UNSUCCESS_VALIDATE_TOKEN, userId,token);

        return Response.ok(tokenValidation).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTokens(AuthenticationRequest credentials) {
        try {
            Long userId = userService.authenticateUser(credentials.getUsername(), credentials.getPassword());
            Tokens tokens = jwtService.generateAndSaveTokens(userId);
            return Response.ok(tokens).build();

        } catch (UserUnauthorizedException unauthorized) {
            LOGGER.warn(LOGGER_MESSAGE_UNSUCCESS_LOGIN, credentials.getUsername());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTokens(RefreshTokenRequest refreshToken) {
        Tokens newTokens;
        try {
            newTokens = jwtService.refreshTokens(refreshToken);
        } catch (BadTokenException e) {
            throw new WebApplicationException("bad token " + e.getMessage(), Response.Status.UNAUTHORIZED);
        }
        return Response.ok(newTokens).build();
    }
}
