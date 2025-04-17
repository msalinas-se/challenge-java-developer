package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "DTO de respuesta devuelto al crear un usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    @Schema(description = "Identificador único del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Fecha de creación del usuario", example = "2025-04-16T17:00:00")
    private LocalDateTime created;

    @Schema(description = "Fecha de última modificación", example = "2025-04-16T17:00:00")
    private LocalDateTime modified;

    @Schema(description = "Fecha de último ingreso", example = "2025-04-16T17:00:00")
    private LocalDateTime lastLogin;

    @Schema(description = "Token de acceso generado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Indica si el usuario está activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Nombre completo del usuario", example = "Juan Rodriguez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example =  "juan@rodriguez.org")
    private String email;

    @Schema(description = "Listado de teléfonos asociados al usuario")
    private List<PhoneDTO> phones;
}
