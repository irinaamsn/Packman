package org.packman.server.logic

import org.packman.server.config.DatabaseConfig.getUserService
import org.packman.server.dto.AppUserDto
import org.packman.server.services.UserService


class Database() {
    private var userService: UserService = getUserService();
    fun getCurrentPosition(username: String, points: Int): Int = userService.getPosition(username, points)

    fun getBestPlayers(): List<AppUserDto> = userService.getTopPlayers(COUNT_BEST_PLAYERS)

    companion object {
        private const val COUNT_BEST_PLAYERS = 10
    }
}