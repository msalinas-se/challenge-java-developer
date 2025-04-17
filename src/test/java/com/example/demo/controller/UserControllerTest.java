package com.example.demo.controller;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserResponseDTO sampleResponse() {
        return UserResponseDTO.builder()
                .id(UUID.randomUUID())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("token123")
                .isActive(true)
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .phones(List.of(new PhoneDTO("1234567", "1", "57")))
                .build();
    }

    private UserRequestDTO sampleRequest() {
        return UserRequestDTO.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .password("Hunter123")
                .phones(List.of(new PhoneDTO("1234567", "1", "57")))
                .build();
    }

    @Test
    @DisplayName("POST /api/users - Success")
    void registerUser_success() throws Exception {
        UserResponseDTO response = sampleResponse();
        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.token").value(response.getToken()));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Success")
    void getUser_success() throws Exception {
        UserResponseDTO response = sampleResponse();
        UUID id = response.getId();
        Mockito.when(userService.getUser(eq(id))).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - Success")
    void updateUser_success() throws Exception {
        UserResponseDTO response = sampleResponse();
        UUID id = response.getId();
        Mockito.when(userService.updateUser(eq(id), any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Success")
    void deleteUser_success() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(userService).deleteUser(eq(id));

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
    }
}
