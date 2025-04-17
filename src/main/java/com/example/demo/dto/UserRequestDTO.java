package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de solicitud para registrar un usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @Schema(description = "Nombre completo del usuario", example = "Juan Rodriguez", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example =  "juan@rodriguez.org", required = true)
    @Email(regexp = "${app.validation.email-regex}", message = "El formato de correo es inválido")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "hunter2", required = true)
    @Pattern(regexp = "${app.validation.password-regex}", message = "El formato de contraseña es inválido")
    private String password;

    @Schema(description = "Listado de teléfonos asociados al usuario")
    private List<PhoneDTO> phones;
}
