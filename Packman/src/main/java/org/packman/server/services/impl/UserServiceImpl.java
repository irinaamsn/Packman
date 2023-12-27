package org.packman.server.services.impl;

import lombok.RequiredArgsConstructor;
import org.packman.server.dto.AppUserDto;
import org.packman.server.exceptions.NotFoundException;
import org.packman.server.mappers.UserMapper;
import org.packman.server.models.AppUser;
import org.packman.server.repositories.UserRepository;
import org.packman.server.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public int getPosition(String username, int points) {
        if (!userRepository.existsByUsernameAndBestPoints(username,points)) {
            AppUser newAppUser = new AppUser(username, points);
            userRepository.addUser(newAppUser);
        }
        return userRepository.getPositionByUsernameAndPoints(username, points).orElseThrow(
                () -> new NotFoundException(400, "Position not found", System.currentTimeMillis()));
    }

    @Override
    public List<AppUserDto> getTopPlayers(int countPlayers) {
        return userRepository.getTopNUsers(PageRequest.of(0, countPlayers)).stream()
                .map(userMapper::toAppUserDto)
                .toList();
    }
}
