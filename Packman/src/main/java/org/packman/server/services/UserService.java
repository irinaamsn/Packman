package org.packman.server.services;

import org.packman.server.dto.AppUserDto;
import org.packman.server.models.AppUser;

import java.util.List;

public interface UserService {
    int getPosition(String username, int points);
    List<AppUserDto> getTopPlayers(int countPlayers);
}
