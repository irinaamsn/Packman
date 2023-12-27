package org.packman.server.mappers.impl;

import org.packman.server.dto.AppUserDto;
import org.packman.server.exceptions.MapperCovertException;
import org.packman.server.mappers.UserMapper;
import org.packman.server.models.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public AppUserDto toAppUserDto(AppUser user) {
        if (user == null)
            throw new MapperCovertException(400, "Invalid data user", System.currentTimeMillis());
        AppUserDto userDto = new AppUserDto();
        userDto.setUsername(user.getUsername());
        userDto.setBestPoints(user.getBestPoints());
        return userDto;
    }
}
