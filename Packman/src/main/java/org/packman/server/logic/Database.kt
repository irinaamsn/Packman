package org.packman.server.logic

import org.packman.server.config.DatabaseConfig.getUserService
import org.packman.server.dto.AppUserDto
import org.packman.server.services.UserService


class Database() {
    private var userService: UserService = getUserService();
    fun getCurrentPosition(username: String, points: Int): Int {
        // @TODO Make logic with to save all result of games
//        return userService.getPosition(username, points);
        return 10
    }

    fun getBestPlayers(): List<AppUserDto> {
        // @TODO Make logic with to get best players
//        return userService.getTopPlayers(COUNT_BEST_PLAYERS)
        return listOf(
                AppUserDto("Lola1", 1000),
                AppUserDto("Lola2", 900),
                AppUserDto("Lola3", 500),
                AppUserDto("Lola4", 100),
        )
    }

    private val COUNT_BEST_PLAYERS = 10
}