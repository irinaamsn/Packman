package org.packman.server.logic

object Database {
    fun getCurrentPosition(points: Player): Int {
        // @TODO Make logic with to save all result of games
        return 10
    }

    fun getBestPlayers(): List<BestPlayer> {
        // @TODO Make logic with to get best players
        return listOf(
                BestPlayer("Lola1", 1000),
                BestPlayer("Lola2", 900),
                BestPlayer("Lola3", 500),
                BestPlayer("Lola4", 100),
        )
    }
}