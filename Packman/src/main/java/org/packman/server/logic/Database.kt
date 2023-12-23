package org.packman.server.logic

object Database {
    fun getCurrentPosition(name: String, points: Int): Int {

        return 10
    }

    fun getBestPlayers(): List<BestPlayer> {

        return listOf(
            BestPlayer("Lola1", 1000),
            BestPlayer("Lola2", 900),
            BestPlayer("Lola3", 500),
            BestPlayer("Lola4", 100),
        )
    }

    private const val MAX_COUNT_BEST_PLAYERS = 10
}