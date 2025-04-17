package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operaciones de gestión de usuarios")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Obtiene un usuario por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@Parameter(description = "UUID del usuario", required = true) @PathVariable("id") UUID id) {
        log.info("GET /api/users/{} called", id);
        UserResponseDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Registra un nuevo usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Validación fallida o correo duplicado")
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("POST /api/users called with name={} email={}", userRequestDTO.getName(), userRequestDTO.getEmail());
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseDTO);
    }

    @Operation(
        summary = "Actualiza un usuario existente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida o correo duplicado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "UUID del usuario", required = true) @PathVariable("id") UUID id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("PUT /api/users/{} called with update data name={} email={}", id, userRequestDTO.getName(), userRequestDTO.getEmail());
        UserResponseDTO updated = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Elimina un usuario por ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "UUID del usuario", required = true) @PathVariable("id") UUID id) {
        log.info("DELETE /api/users/{} called", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
