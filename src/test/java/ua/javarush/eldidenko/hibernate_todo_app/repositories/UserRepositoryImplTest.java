package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import liquibase.exception.LiquibaseException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.init.HibernateSessionProviderInit;
import ua.javarush.eldidenko.hibernate_todo_app.init.LiquibaseConfigurationInit;
import ua.javarush.eldidenko.hibernate_todo_app.provider.SessionProvider;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DisplayName("UserRepository")
public class UserRepositoryImplTest {

    private static UserRepository userRepository;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13-alpine")
            .withDatabaseName("todo")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeAll
    public static void startDb() throws SQLException, LiquibaseException {
        postgreSQLContainer.start();
        new LiquibaseConfigurationInit(postgreSQLContainer).update();
        SessionProvider sessionProvider = new HibernateSessionProviderInit(postgreSQLContainer);
        SessionFactory sessionFactory = sessionProvider.getSessionFactory();
        userRepository = new UserRepositoryImpl(sessionFactory);

        System.setProperty("dbunit.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("dbunit.driver", postgreSQLContainer.getDriverClassName());
        System.setProperty("dbunit.user", postgreSQLContainer.getUsername());
        System.setProperty("dbunit.password", postgreSQLContainer.getPassword());
    }

    @Test
    @DataSet(value = "datasets/users/default_user.yml", cleanAfter = true)
    public void should_return_existent_user_by_id() {
        User testUser = User.builder()
                .id(1L)
                .username("Bob")
                .password("password")
                .tasks(List.of())
                .build();
        assertEquals(testUser, userRepository.fetchById(1L));
    }


    @Test
    @DataSet(value = "datasets/users/default_user.yml", cleanAfter = true)
    public void should_return_null_non_existent_user_by_id() {
        assertNull(userRepository.fetchById(10L));
    }

    @Test
    @DataSet(value = "datasets/users/default_user.yml", cleanAfter = true)
    public void should_return_existent_user_by_username() {
        User testUser = User.builder()
                .id(1L)
                .username("Bob")
                .password("password")
                .tasks(List.of())
                .build();
        assertEquals(testUser, userRepository.fetchByUserName("Bob"));
    }

    @Test
    @DataSet(value = "datasets/users/default_user.yml", cleanAfter = true)
    public void should_return_null_non_existent_user_by_username() {
        assertEquals(null, userRepository.fetchByUserName("Qwerty"));
    }

    @Test
    void save() {
        User newUser = User.builder()
                //.id(5L)
                .username("Bob")
                .password("password")
                .tasks(List.of())
                .build();
        assertNull(newUser.getId());
        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser.getId());
        assertEquals("Bob", userRepository.fetchByUserName("Bob").getUsername());
        assertEquals(newUser, savedUser);
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}
