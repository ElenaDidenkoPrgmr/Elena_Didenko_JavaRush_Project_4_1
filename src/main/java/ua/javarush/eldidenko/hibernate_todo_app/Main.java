package ua.javarush.eldidenko.hibernate_todo_app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URI;

import ua.javarush.eldidenko.hibernate_todo_app.exceptions.IllegalArgumentExceptionMapper;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.ServerExceptionMapper;
import ua.javarush.eldidenko.hibernate_todo_app.migration.LiquibaseConfiguration;
import ua.javarush.eldidenko.hibernate_todo_app.provider.HibernateSessionProvider;
import ua.javarush.eldidenko.hibernate_todo_app.provider.SessionProvider;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.*;
import ua.javarush.eldidenko.hibernate_todo_app.services.*;

import static ua.javarush.eldidenko.hibernate_todo_app.constants.AppConstants.*;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static final String BASE_URI = "http://localhost:8080/";

   /* @PostConstruct
    public void init() {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }*/

    public static HttpServer startServer() {
        new LiquibaseConfiguration().update();

        SessionProvider sessionProvider = new HibernateSessionProvider();
        SessionFactory sessionFactory = sessionProvider.getSessionFactory();

        UserRepository userRepository = new UserRepositoryImpl(sessionFactory);
        UserService userService = new UserServiceImpl(userRepository);
        TokenRepository tokenRepository = new TokenRepositoryImpl(sessionFactory);
        JwtService jwtService = new JwtServiceImpl(tokenRepository, userRepository);
        TaskRepository taskRepository = new TaskRepositoryImpl(sessionFactory);
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);

        final ResourceConfig rc = new ResourceConfig()
                .register(new LoggingFeature(java.util.logging.Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                        java.util.logging.Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000))
                .register(IllegalArgumentExceptionMapper.class)
                .register(ServerExceptionMapper.class)

                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
                .property(SESSION_FACTORY, sessionFactory)
                .property(USER_SERVICE, userService)
                .property(JWT_SERVICE, jwtService)
                .property(TASK_SERVICE, taskService)
                .packages("ua.javarush.eldidenko.hibernate_todo_app.rest");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        LOGGER.debug(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}
