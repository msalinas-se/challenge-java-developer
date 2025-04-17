package com.example.demo.mapper;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.Phone;
import com.example.demo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    @DisplayName("toEntity(null) debe devolver null")
    void toEntity_nullInput() {
        assertNull(UserMapper.toEntity(null), "Se esperaba null al mapear null a entidad");
    }

    @Test
    @DisplayName("toEntity debe mapear campos correctamente usando mocks de PhoneDTO")
    void toEntity_mappingWithMockPhoneDTO() {
        // Crear mock de PhoneDTO
        PhoneDTO phoneDtoMock = Mockito.mock(PhoneDTO.class);
        Mockito.when(phoneDtoMock.getNumber()).thenReturn("9999");
        Mockito.when(phoneDtoMock.getCitycode()).thenReturn("2");
        Mockito.when(phoneDtoMock.getCountrycode()).thenReturn("54");

        UserRequestDTO dto = UserRequestDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .password("Password1")
                .phones(List.of(phoneDtoMock))
                .build();

        User user = UserMapper.toEntity(dto);

        assertNotNull(user, "Se esperaba que la entidad no fuera null");
        assertEquals(dto.getName(), user.getName(), "El nombre debe mapearse correctamente");
        assertEquals(dto.getEmail(), user.getEmail(), "El correo debe mapearse correctamente");
        assertEquals(dto.getPassword(), user.getPassword(), "La contraseña debe mapearse correctamente");

        assertNotNull(user.getPhones(), "Se esperaba lista de teléfonos no nula");
        assertEquals(1, user.getPhones().size(), "Debe haber un teléfono mapeado");
        Phone phone = user.getPhones().get(0);
        assertEquals("9999", phone.getNumber(), "El número debe coincidir con el mock");
        assertEquals("2", phone.getCitycode(), "El código de ciudad debe coincidir con el mock");
        assertEquals("54", phone.getCountrycode(), "El código de país debe coincidir con el mock");
        assertSame(user, phone.getUser(), "El back-reference debe apuntar al usuario padre");
    }

    @Test
    @DisplayName("toResponse(null) debe devolver null")
    void toResponse_nullInput() {
        assertNull(UserMapper.toResponse(null), "Se esperaba null al mapear null a DTO de respuesta");
    }

    @Test
    @DisplayName("toResponse debe mapear campos de User a UserResponseDTO correctamente")
    void toResponse_mapping() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Sample")
                .email("sample@domain.com")
                .password("ignored")
                .created(LocalDateTime.of(2025, 4, 17, 12, 0))
                .modified(LocalDateTime.of(2025, 4, 17, 12, 5))
                .lastLogin(LocalDateTime.of(2025, 4, 17, 12, 10))
                .token("tok123")
                .isActive(true)
                .phones(List.of(
                        Phone.builder()
                                .number("1111")
                                .citycode("3")
                                .countrycode("58")
                                .user(null)
                                .build()
                ))
                .build();

        UserResponseDTO dto = UserMapper.toResponse(user);

        assertNotNull(dto, "Se esperaba DTO de respuesta no nulo");
        assertEquals(user.getId(), dto.getId(), "El ID debe mapearse correctamente");
        assertEquals(user.getCreated(), dto.getCreated(), "La fecha de creación debe mapearse correctamente");
        assertEquals(user.getModified(), dto.getModified(), "La fecha de modificación debe mapearse correctamente");
        assertEquals(user.getLastLogin(), dto.getLastLogin(), "La fecha de último login debe mapearse correctamente");
        assertEquals(user.getToken(), dto.getToken(), "El token debe mapearse correctamente");
        assertEquals(user.getIsActive(), dto.getIsActive(), "El estado activo debe mapearse correctamente");
        assertEquals(user.getName(), dto.getName(), "El nombre debe mapearse correctamente");
        assertEquals(user.getEmail(), dto.getEmail(), "El correo debe mapearse correctamente");
        assertNotNull(dto.getPhones(), "Se esperaba lista de teléfonos en DTO de respuesta");
        assertEquals(1, dto.getPhones().size(), "Debe mapearse un teléfono");
        PhoneDTO phoneDto = dto.getPhones().get(0);
        assertEquals("1111", phoneDto.getNumber(), "El número debe mapearse correctamente");
        assertEquals("3", phoneDto.getCitycode(), "El código de ciudad debe mapearse correctamente");
        assertEquals("58", phoneDto.getCountrycode(), "El código de país debe mapearse correctamente");
    }
}
