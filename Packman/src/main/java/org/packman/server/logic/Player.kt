package org.packman.server.logic

data class Player(
    val name: String,
    val gameStartedTime: Long,
    var countPoints: Int,
    val map: PlayerMap,
) {
    companion object {
        internal const val DEFAULT_NAME = "Unknown"
    }
}

data class PlayerMap(
    val map: MutableList<MutableList<Int>>,
    val coordinatePlayer: Coordinate,
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