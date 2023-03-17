package ua.javarush.eldidenko.hibernate_todo_app.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(Response.Status.EXPECTATION_FAILED)
                .entity(build(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private RestErrorResponse build(String message) {
        RestErrorResponse response = new RestErrorResponse();
        response.setMessage("an illegal argument was provided: " + message);
        return response;
    }
}
