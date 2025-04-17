package com.example.demo.mapper;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.entity.Phone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneMapperTest {

    @Test
    @DisplayName("toEntity(null) debe devolver null")
    void toEntity_nullDto() {
        Phone result = PhoneMapper.toEntity(null);
        assertNull(result, "Se esperaba null al mapear null a entidad");
    }

    @Test
    @DisplayName("toEntity y toDTO deben mapear los campos correctamente")
    void toEntity_and_toDTO_mapping() {
        PhoneDTO dto = PhoneDTO.builder()
                .number("1234567")
                .citycode("1")
                .countrycode("57")
                .build();

        Phone entity = PhoneMapper.toEntity(dto);

        assertNotNull(entity, "Se esperaba que la entidad no fuera null");
        assertEquals(dto.getNumber(), entity.getNumber(), "El número debe coincidir con el DTO");
        assertEquals(dto.getCitycode(), entity.getCitycode(), "El código de ciudad debe coincidir con el DTO");
        assertEquals(dto.getCountrycode(), entity.getCountrycode(), "El código de país debe coincidir con el DTO");

        PhoneDTO backDto = PhoneMapper.toDTO(entity);

        assertNotNull(backDto, "Se esperaba que el DTO resultante no fuera null");
        assertEquals(entity.getNumber(), backDto.getNumber(), "El número debe mapearse correctamente de la entidad al DTO");
        assertEquals(entity.getCitycode(), backDto.getCitycode(), "El código de ciudad debe mapearse correctamente de la entidad al DTO");
        assertEquals(entity.getCountrycode(), backDto.getCountrycode(), "El código de país debe mapearse correctamente de la entidad al DTO");
    }

    @Test
    @DisplayName("toDTO(null) debe devolver null")
    void toDTO_nullEntity() {
        PhoneDTO result = PhoneMapper.toDTO(null);
        assertNull(result, "Se esperaba null al mapear null a DTO");
    }
}
