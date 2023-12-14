package org.packman.server.logic

import java.util.concurrent.TimeUnit

class MapLogic {

    fun createMap(): PlayerMap {
        // @TODO(Adel Grishin) add logic to create map and coins
        return PlayerMap(
            map = mutableListOf(),
            lifeCoins = mutableListOf(),
            coordinatePlayer = Coordinate(i = 0, j = 0)
        )
    }

    fun movePlayer(player: Player, command: Move): PlayerMap? {
        val playerMap = player.map
        val updateMap: PlayerMap = updateMap(playerMap) ?: playerMap

        val coordinatePlayer = playerMap.coordinatePlayer

        val newCoordinatePlayer = when (command) {
            Move.UP -> Coordinate(i = coordinatePlayer.i + 1, j = coordinatePlayer.j)
            Move.DOWN -> Coordinate(i = coordinatePlayer.i - 1, j = coordinatePlayer.j)
            Move.LEFT -> Coordinate(i = coordinatePlayer.i, j = coordinatePlayer.j - 1)
            Move.RIGHT -> Coordinate(i = coordinatePlayer.i, j = coordinatePlayer.j + 1)
        }

        //@TODO (Adel Grishin) make parse
        if (!isMovePossible(newCoordinatePlayer)) return null

        val obj = ParseMap.parse(updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j])
        when(obj) {
            ParseMap.COIN, ParseMap.WEAK_COIN -> {
                player.countPoints += PRICE_COIN
                player.map.lifeCoins.removeIf { coin ->
                    coin.coordinate.i == newCoordinatePlayer.i && coin.coordinate.j == newCoordinatePlayer.j
                }
                updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j] = ParseMap.PLAYER.value
                updateMap.map[coordinatePlayer.i][coordinatePlayer.j] = ParseMap.EMPTY.value
            }

            ParseMap.EMPTY -> {
                updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j] = ParseMap.PLAYER.value
                updateMap.map[coordinatePlayer.i][coordinatePlayer.j] = ParseMap.EMPTY.value
            }
            else -> Unit
        }

        //@TODO (Adel Grishin) make parse
        return null
    }

    fun updateMap(playerMap: PlayerMap): PlayerMap? {
        val currentTime = System.currentTimeMillis()
        val map = playerMap.map
        val coins = playerMap.lifeCoins
        val newCoins = mutableListOf<Coin>()
        for (coin in coins) {
            val leftTime = getLifeCoin(currentTime, coin.timeCreated)
            when (leftTime) {
                in TIME_LIFE_CHANGE_COLOR_COIN_MS..TIME_LIFE_ONE_COIN_MS -> {
                    newCoins.add(coin)
                }

                in 0..TIME_LIFE_CHANGE_COLOR_COIN_MS -> {
                    map[coin.coordinate.i][coin.coordinate.j] = ParseMap.WEAK_COIN.value
                    newCoins.add(coin)
                }

                else -> {
                    map[coin.coordinate.i][coin.coordinate.j] = ParseMap.EMPTY.value
                }
            }
        }
        return if (coins.size != newCoins.size) {
            playerMap.lifeCoins = newCoins
            playerMap
        } else {
            null
        }
    }

    private fun getLifeCoin(createdTime: Long, currentTime: Long): Long =
        createdTime + TIME_LIFE_ONE_COIN_MS - currentTime

    private fun isMovePossible(coordinate: Coordinate) =
        !(coordinate.i < 0 || coordinate.i >= HEIGHT || coordinate.j < 0 || coordinate.j >= WIDTH)

    private val TIME_LIFE_ONE_COIN_MS = TimeUnit.SECONDS.toMillis(10)
    private val TIME_LIFE_CHANGE_COLOR_COIN_MS = TimeUnit.SECONDS.toMillis(3)

    private val PRICE_COIN = 10

    private enum class ParseMap(val value: Int) {
        EMPTY(0),
        PLAYER(1),
        WALL(2),
        COIN(3),
        WEAK_COIN(4);

        companion object {
            fun parse(value: Int) = when(value) {
                0 -> EMPTY
                1 -> PLAYER
                2 -> WALL
                3 -> COIN
                4 -> WEAK_COIN
                else -> EMPTY
            }
        }
    }

    private val WIDTH = 20
    private val HEIGHT = 15
}