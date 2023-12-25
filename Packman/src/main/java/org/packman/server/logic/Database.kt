package org.packman.server.logic

import lombok.RequiredArgsConstructor
import org.packman.server.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@RequiredArgsConstructor
class Database @Autowired constructor(private val userService: UserService){
    fun getCurrentPosition(points: Player): Int {
        // @TODO Make logic with to save all result of games
//        return userService.getPosition(username,points);
        return 10;
    }

    fun getBestPlayers(): List<BestPlayer> {
        // @TODO Make logic with to get best players
//        return userService.getTopPlayers(10);
        return listOf(
                BestPlayer("Lola1", 1000),
                BestPlayer("Lola2", 900),
                BestPlayer("Lola3", 500),
                BestPlayer("Lola4", 100),
        )
    }
}