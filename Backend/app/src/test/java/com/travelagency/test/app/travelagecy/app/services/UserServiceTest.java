package com.travelagency.test.app.travelagecy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.UserRepository;
import com.travelagency.app.services.UserService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("USR-123");
        user.setUsername("testuser");
        user.setEmail("test@travel.com");
    }

    @Test
    void saveUser_Exitoso() throws Exception {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.saveUser(user);
        assertThat(result).isNotNull();
    }

    @Test
    void saveUser_EmailDuplicado() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertThatThrownBy(() -> userService.saveUser(user)).isInstanceOf(Exception.class);
    }

    @Test
    void saveUser_IdDuplicado() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsById(anyString())).thenReturn(true);
        assertThatThrownBy(() -> userService.saveUser(user)).isInstanceOf(Exception.class);
    }

    @Test
    void updateUser_Exitoso() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        User result = userService.updateUser(user);
        assertThat(result).isNotNull();
    }

    @Test
    void updateUser_NoEncontrado() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertThatThrownBy(() -> userService.updateUser(user)).isInstanceOf(Exception.class);
    }

    @Test
    void findByUsername_Exitoso() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        User result = userService.findByUsername("testuser");
        assertThat(result).isNotNull();
    }

    @Test
    void findByUsername_NoEncontrado() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertThatThrownBy(() -> userService.findByUsername("wrong")).isInstanceOf(Exception.class);
    }
}