package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.ValidationException;
import com.example.demo.entity.Phone;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Override
    public UserResponseDTO getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        // Verificar correo duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }
        // Mapear DTO a entidad
        User user = UserMapper.toEntity(dto);

        // Persistir user para generar ID
        User savedUser = userRepository.save(user);

        // Generar JWT token usando el ID generado
        String jwtToken = Jwts.builder()
            .setSubject(savedUser.getId().toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
        savedUser.setToken(jwtToken);
        savedUser.setCreated(LocalDateTime.now());
        savedUser.setLastLogin(LocalDateTime.now());
        savedUser.setIsActive(true);

        // Actualizar user con el token generado
        User createdUser = userRepository.save(savedUser);
        return UserMapper.toResponse(createdUser);
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Usuario no encontrado"));
        if (!existing.getEmail().equals(dto.getEmail()) &&
            userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setPhones(dto.getPhones() == null ? List.of() :
            dto.getPhones().stream()
                .map(p -> Phone.builder()
                    .number(p.getNumber())
                    .citycode(p.getCitycode())
                    .countrycode(p.getCountrycode())
                    .build())
                .collect(Collectors.toList()));
        existing.setModified(LocalDateTime.now());
        existing.setLastLogin(LocalDateTime.now());
        User updated = userRepository.save(existing);
        return UserMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
}
