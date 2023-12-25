package org.packman.server.logic

import lombok.RequiredArgsConstructor
import org.packman.server.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class Database @Autowired constructor(private val userService: UserService) {
    companion object {
        fun getCurrentPosition(username: String, points: Int): Int {
            // @TODO Make logic with to save all result of games
//        return userService.getPosition(username,points);
            return 10;
        }

        fun getBestPlayers(): List<BestPlayer> {
            // @TODO Make logic with to get best players
//        return userService.getTopPlayers(MAX_COUNT_BEST_PLAYERS);
            return listOf(
                    BestPlayer("Lola1", 1000),
                    BestPlayer("Lola2", 900),
                    BestPlayer("Lola3", 500),
                    BestPlayer("Lola4", 100),
            )
        }
    }
//    private count val MAX_COUNT_BEST_PLAYERS = 10
}