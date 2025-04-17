package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;

import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO getUser(UUID id);

    UserResponseDTO updateUser(UUID id, UserRequestDTO dto);
    void deleteUser(UUID id);
}
