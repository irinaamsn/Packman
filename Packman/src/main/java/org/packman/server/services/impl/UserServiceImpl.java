package org.packman.server.services.impl;

import lombok.RequiredArgsConstructor;
import org.packman.server.exceptions.NotFoundException;
import org.packman.server.models.AppUser;
import org.packman.server.repositories.UserRepository;
import org.packman.server.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public int getPosition(String username, int points) {
        AppUser newAppUser = new AppUser(username, points);
        userRepository.addUser(newAppUser);
        return userRepository.getPositionByUsernameAndPoints(username).orElseThrow(
                () -> new NotFoundException(400, "Position not found", System.currentTimeMillis()));
    }

    @Override
    public List<AppUser> getTopPlayers(int countPlayers) {
        return userRepository.getTopNUsers(PageRequest.of(0, countPlayers));
    }
}
