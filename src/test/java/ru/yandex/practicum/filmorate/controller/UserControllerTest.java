package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendshipDaoImpl;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {
    static UserController userController;
    static UserController userControllerDb;
    User user;
    private static Validator validator;
    Set<ConstraintViolation<User>> violations;

    @BeforeAll
    static void initialization() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @BeforeEach
    void configUsers() {
        userController = new UserController(new UserService( new InMemoryUserStorage()));
        userControllerDb = new UserController(
                new UserService(new UserDbStorage(new JdbcTemplate(),new FriendshipDaoImpl(new JdbcTemplate()))));

        user = User.builder()
                .email("name@email.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1900, 10, 05))
                .build();
    }
    @AfterEach
    void clean(){
        violations = null;
    }

    @Test
    void shouldCreateAndValidateUser(){
        userController.createUser(user);
        assertEquals(1,userController.allUsers().size());
        assertEquals("login",userController.allUsers().get(0).getLogin() );
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @MethodSource("testLogin")
    @ParameterizedTest(name = "{index} All position of space-{0}")
    void shouldNotValidateUserWithEmptyOrWithSpaceLogin(String login){
        user.setLogin("");
        violations = validator.validate(user);
        assertEquals(1,violations.size());
        assertEquals("Login должен быть заполнен",violations.iterator().next().getMessage());
        user.setLogin(login);
        violations = validator.validate(user);
        assertEquals("Логин не должен содержать пробелов",violations.iterator().next().getMessage());
        assertThrows(IllegalStateException.class, () -> userControllerDb.createUser(user));
    }
    private static Stream<Arguments> testLogin() {
        return Stream.of(
                Arguments.of("l o"),
                Arguments.of(" lo"),
                Arguments.of("lo ")
        );
    }

    @Test
    void shouldNotValidUserWithBlankOrNotRightEmail(){
        user.setEmail("");
        violations = validator.validate(user);
        assertEquals(1,violations.size());
        assertEquals("Email должен быть заполнен",violations.iterator().next().getMessage());
        user.setEmail("@ma.");
        violations = validator.validate(user);
        assertEquals(1,violations.size());
        assertEquals("Email должен иметь формат адреса электронной почты",violations.iterator().next().getMessage());
        assertThrows(IllegalStateException.class, () -> userControllerDb.createUser(user));
    }

    @Test
    void shouldNotValidateUserWithBirthdayInFuture(){
        user.setBirthday(LocalDate.now());
        violations = validator.validate(user);
        assertEquals(1,violations.size());
        assertEquals("Дата рождения указана неверно",violations.iterator().next().getMessage());
        assertThrows(IllegalStateException.class, () -> userControllerDb.createUser(user));
    }

    @Test
    void shouldRenameUserWithEmptyName(){
        user.setName("");
        userController.createUser(user);
        assertEquals("login",user.getName());
    }

    @Test
    void shouldThrowExceptionIfUserIdNotFound(){
        userController.createUser(user);
        user.setId(100l);
        assertThrows(NoSuchUserIdException.class, () -> userController.updateUser(user));
    }

    @Test
    void shouldReturnBadRequestIfUserBodyEmpty(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }
}

