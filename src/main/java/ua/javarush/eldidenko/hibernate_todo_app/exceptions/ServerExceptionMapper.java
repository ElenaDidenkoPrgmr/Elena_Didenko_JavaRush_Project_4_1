package ua.javarush.eldidenko.hibernate_todo_app.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ServerExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        String message = exception.getMessage();
        Response response = exception.getResponse();
        Response.Status status = response.getStatusInfo().toEnum();

        return Response.status(status)
                .entity(status + ": " + message)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
