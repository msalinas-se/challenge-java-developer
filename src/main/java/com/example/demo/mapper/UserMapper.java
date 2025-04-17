package com.example.demo.mapper;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.Phone;
import com.example.demo.entity.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .build();
        List<Phone> phones = dto.getPhones() == null ? List.of() :
            dto.getPhones().stream()
                .map(p -> Phone.builder()
                    .number(p.getNumber())
                    .citycode(p.getCitycode())
                    .countrycode(p.getCountrycode())
                    .user(user)
                    .build())
                .collect(Collectors.toList());
        user.setPhones(phones);
        return user;
    }

    public UserResponseDTO toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
            .id(user.getId())
            .created(user.getCreated())
            .modified(user.getModified())
            .lastLogin(user.getLastLogin())
            .token(user.getToken())
            .isActive(user.getIsActive())
            .name(user.getName())
            .email(user.getEmail())
            .phones(user.getPhones() == null ? List.of() :
                user.getPhones().stream()
                    .map(p -> PhoneDTO.builder()
                        .number(p.getNumber())
                        .citycode(p.getCitycode())
                        .countrycode(p.getCountrycode())
                        .build())
                    .collect(Collectors.toList()))
            .build();
    }
}
