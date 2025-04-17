package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("createUser lanza DuplicateEmailException si el email ya existe")
    void createUser_duplicateEmail() {
        UserRequestDTO dto = UserRequestDTO.builder().email("dup@example.com").build();
        when(userRepository.existsByEmail(eq("dup@example.com"))).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(dto),
            "Se esperaba DuplicateEmailException cuando el email ya existe");
    }

    @Test
    @DisplayName("getUser devuelve UserResponseDTO cuando el usuario existe")
    void getUser_success() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUser(id);
        assertNotNull(result, "El DTO de respuesta no debe ser null");
        assertEquals(id, result.getId(), "El ID debe coincidir");
    }

    @Test
    @DisplayName("getUser lanza ValidationException cuando no encuentra usuario")
    void getUser_notFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> userService.getUser(id),
            "Se esperaba ValidationException cuando el usuario no existe");
    }

    @Test
    @DisplayName("deleteUser elimina sin excepción cuando el usuario existe")
    void deleteUser_success() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(eq(id))).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(id));
        verify(userRepository).deleteById(eq(id));
    }

    @Test
    @DisplayName("deleteUser lanza ValidationException cuando el usuario no existe")
    void deleteUser_notFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(eq(id))).thenReturn(false);

        assertThrows(ValidationException.class, () -> userService.deleteUser(id),
            "Se esperaba ValidationException cuando el usuario no existe");
    }

    @Test
    @DisplayName("updateUser lanza ValidationException si el usuario no existe")
    void updateUser_notFound() {
        UUID id = UUID.randomUUID();
        UserRequestDTO dto = UserRequestDTO.builder().email("test@example.com").phones(List.of()).build();
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> userService.updateUser(id, dto),
            "Se esperaba ValidationException cuando el usuario no existe");
    }
}
