package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.PhoneMapper;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.entity.Phone;

import javax.crypto.SecretKey;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Override
    public UserResponseDTO getUser(UUID id) {
        log.info("Iniciando getUser - id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));
        return UserMapper.toResponse(user);
    }

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        log.info("Iniciando createUser - dto={}", dto);
        // Verificar correo duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }
        // Mapear DTO a entidad
        User user = UserMapper.toEntity(dto);
        user.getPhones().forEach(phone -> phone.setUser(user));

        // Persistir user para generar ID
        User savedUser = userRepository.save(user);

        // Generar JWT token usando el ID generado
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jwtToken = Jwts.builder()
            .setSubject(savedUser.getId().toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
        savedUser.setToken(jwtToken);
        savedUser.setCreated(LocalDateTime.now());
        savedUser.setLastLogin(LocalDateTime.now());
        savedUser.setIsActive(true);

        // Actualizar user con el token generado
        User createdUser = userRepository.save(savedUser);
        return UserMapper.toResponse(createdUser);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        log.info("Iniciando updateUser - dto={}", dto);
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Usuario no encontrado"));
        if (!existing.getEmail().equals(dto.getEmail()) &&
            userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());

        // Convertir cada PhoneDTO a Phone usando PhoneMapper y asignar la referencia al usuario
        List<Phone> mergedPhones = dto.getPhones().stream()
            .map(PhoneMapper::toEntity)
            .peek(phone -> phone.setUser(existing))
            .collect(Collectors.toList());
        // Reemplazar la lista de teléfonos (orphanRemoval eliminará los no referenciados)
        existing.getPhones().clear();
        existing.getPhones().addAll(mergedPhones);

        existing.setModified(LocalDateTime.now());
        existing.setLastLogin(LocalDateTime.now());
        User updated = userRepository.save(existing);
        return UserMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Iniciando deleteUser - id={}", id);
        if (!userRepository.existsById(id)) {
            throw new ValidationException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
}
