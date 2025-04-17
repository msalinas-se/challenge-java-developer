package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información de un teléfono del usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO {
    @Schema(description = "Número de teléfono", example = "1234567")
    private String number;

    @Schema(description = "Código de ciudad", example = "1")
    private String citycode;

    @Schema(description = "Código de país", example = "57")
    private String countrycode;
}
