package ua.javarush.eldidenko.hibernate_todo_app.Listener;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.jboss.logging.MDC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

public class ResourcesLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Context
    private ResourceInfo resourceInfo;

    private static final Logger LOGGER = LogManager.getLogger(ResourcesLoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) {
        //Note down the start request time...we will use to calculate the total
        //execution time
        MDC.put("start-time", String.valueOf(System.currentTimeMillis()));

        LOGGER.debug("Entering in Resource : /{} ", requestContext.getUriInfo().getPath());
        LOGGER.debug("Method Name : {} ", resourceInfo.getResourceMethod().getName());
        LOGGER.debug("Class : {} ", resourceInfo.getResourceClass().getCanonicalName());
        logQueryParameters(requestContext);
        logMethodAnnotations();
        logRequestHeader(requestContext);

        //log entity stream...
        String entity = readEntityStream(requestContext);
        if (entity.trim().length() > 0) {
            LOGGER.debug("Entity Stream : {}", entity);
        }
    }

    private void logQueryParameters(ContainerRequestContext requestContext) {
        Iterator<String> iterator = requestContext.getUriInfo().getPathParameters().keySet().iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            List<String> obj = requestContext.getUriInfo().getPathParameters().get(name);
            String value = null;
            if (null != obj && obj.size() > 0) {
                value = (String) obj.get(0);
            }
            LOGGER.debug("Query Parameter Name: {}, Value :{}", name, value);
        }
    }

    private void logMethodAnnotations() {
        Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            LOGGER.debug("----Start Annotations of resource ----");
            for (Annotation annotation : annotations) {
                LOGGER.debug(annotation.toString());
            }
            LOGGER.debug("----End Annotations of resource----");
        }

    }

    private void logRequestHeader(ContainerRequestContext requestContext) {
        Iterator iterator;
        LOGGER.debug("----Start Header Section of request ----");
        LOGGER.debug("Method Type : {}", requestContext.getMethod());
        iterator = requestContext.getHeaders().keySet().iterator();
        while (iterator.hasNext()) {
            String headerName = (String) iterator.next();
            String headerValue = requestContext.getHeaderString(headerName);
            LOGGER.debug("Header Name: {}, Header Value :{} ", headerName, headerValue);
        }
        LOGGER.debug("----End Header Section of request ----");
    }

    private String readEntityStream(ContainerRequestContext requestContext) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final InputStream inputStream = requestContext.getEntityStream();
        final StringBuilder builder = new StringBuilder();
        try {
            ReaderWriter.writeTo(inputStream, outStream);
            byte[] requestEntity = outStream.toByteArray();
            if (requestEntity.length != 0) {
                builder.append(new String(requestEntity));
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
        } catch (IOException ex) {
            LOGGER.debug("----Exception occurred while reading entity stream :{}", ex.getMessage());
        }
        return builder.toString();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext){
        String stTime = (String) MDC.get("start-time");
        if (null == stTime || stTime.length() == 0) {
            return;
        }
        long startTime = Long.parseLong(stTime);
        long executionTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Total request execution time : {} milliseconds", executionTime);
        //clear the context on exit
        MDC.clear();
    }
}
