package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {

    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);

    }

    @Test
    public void whenSaveThenGetSame() {
        var userExpected = new User(1, "user", "user@mail.ru", "password");
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(userExpected));
        Optional<User> user = userService.save(userExpected);
        assertThat(user).isEqualTo(Optional.of(userExpected));
    }

    @Test
    public void whenExistUserWithEmailAndPasswordThenGetUser() {
        var userExpected = new User(1, "user", "user@mail.ru", "password");
        when(userRepository.findByEmailAndPassword("user@mail.ru", "password")).thenReturn(Optional.of(userExpected));
        Optional<User> user = userService.findByEmailAndPassword("user@mail.ru", "password");
        assertThat(user).isEqualTo(Optional.of(userExpected));
    }

    @Test
    public void whenIncorrectEmailOrPasswordThenEmptyOptional() {
        var userExpected = new User(1, "user", "user@mail.ru", "password");
        when(userRepository.findByEmailAndPassword("user@mail.ru", "password")).thenReturn(Optional.of(userExpected));
        Optional<User> user1 = userService.findByEmailAndPassword("user1@mail.ru", "password");
        Optional<User> user2 = userService.findByEmailAndPassword("user@mail.ru", "password1");
        assertThat(user1).isEqualTo(Optional.empty());
        assertThat(user2).isEqualTo(Optional.empty());
    }
}