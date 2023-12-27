package org.packman.server.mappers;

import org.packman.server.dto.AppUserDto;
import org.packman.server.models.AppUser;

public interface UserMapper {
    AppUserDto toAppUserDto(AppUser user);
}
