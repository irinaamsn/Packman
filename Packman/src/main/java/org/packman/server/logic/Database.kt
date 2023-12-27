package org.packman.server.logic

import org.packman.server.config.DatabaseConfig.getUserService
import org.packman.server.dto.AppUserDto
import org.packman.server.services.UserService


class Database() {
    private var userService: UserService = getUserService();
    fun getCurrentPosition(username: String, points: Int): Int {
        // @TODO Make logic with to save all result of games
        val l: Int=userService.getPosition(username, points)
//        return userService.getPosition(username, points)
        return 10
    }

    fun getBestPlayers(): List<BestPlayer> {
        var l:AppUserDto
        // @TODO Make logic with to get best players
//        return userService.getTopPlayers(COUNT_BEST_PLAYERS)
        return listOf(
                BestPlayer("Lola1", 1000),
                BestPlayer("Lola2", 900),
                BestPlayer("Lola3", 500),
                BestPlayer("Lola4", 100)
        )
    }

    private val COUNT_BEST_PLAYERS = 10
}