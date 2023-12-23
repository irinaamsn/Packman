package org.packman.server.logic

data class Player(
    val name: String,
    val gameStartedTime: Long,
    var countPoints: Int,
    var map: PlayerMap,
) {
    companion object {
        internal const val DEFAULT_NAME = "Unknown"
    }
}

data class PlayerMap(
    val map: List<IntArray>,
    var coordinatePlayer: Coordinate,
    var lifeCoins: MutableList<Coin>,
)

data class ClientAddress(
    val ip: String,
    val port: String,
)

data class Coin(
    val timeCreated: Long,
    val coordinate: Coordinate,
)

data class Coordinate(
    val i: Int,
    val j: Int,
)

data class BestPlayer(
    val name: String,
    val countPoints: Int,
) {
    override fun toString(): String = "$name,$countPoints"
}