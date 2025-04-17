package com.example.demo.mapper;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.entity.Phone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneMapper {
    public Phone toEntity(PhoneDTO dto) {
        if (dto == null) {
            return null;
        }
        return Phone.builder()
                .number(dto.getNumber())
                .citycode(dto.getCitycode())
                .countrycode(dto.getCountrycode())
                .build();
    }

    public PhoneDTO toDTO(Phone phone) {
        if (phone == null) {
            return null;
        }
        return PhoneDTO.builder()
                .number(phone.getNumber())
                .citycode(phone.getCitycode())
                .countrycode(phone.getCountrycode())
                .build();
    }
}
