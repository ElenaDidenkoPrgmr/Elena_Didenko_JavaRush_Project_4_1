package ua.javarush.eldidenko.hibernate_todo_app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.BadTokenException;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserUnauthorizedException;
import ua.javarush.eldidenko.hibernate_todo_app.request.AuthenticationRequest;
import ua.javarush.eldidenko.hibernate_todo_app.request.RefreshTokenRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.JwtService;
import ua.javarush.eldidenko.hibernate_todo_app.services.UserService;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.TokenValidation;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.Tokens;

@Path("/authentication")
public class AuthenticationEndpoint {
    private UserService userService;
    private JwtService jwtService;

    @Context
    private void setRc(ResourceConfig rc) {
        userService = (UserService) rc.getProperty("userService");
        jwtService = (JwtService) rc.getProperty("jwtService");
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(@QueryParam("token") String token, @PathParam("userId") Long userId) {
        if (token == null || token.isBlank()) {
            throw new WebApplicationException("token is empty ", Response.Status.UNAUTHORIZED);
        }
        TokenValidation tokenValidation = jwtService.validateAccessTokenByUserId(token, userId);
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
