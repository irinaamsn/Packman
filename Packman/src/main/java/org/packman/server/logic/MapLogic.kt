package org.packman.server.logic

import java.util.concurrent.TimeUnit

class MapLogic {

    fun createMap(): PlayerMap = DifferentMapPlayer().generateMap().generateCoin().generateCoin()

    fun movePlayer(player: Player, command: Move): Player? {
        var wasUpdate: Boolean
        val playerMap = player.map
        val updateMap = updateMap(playerMap) ?: playerMap.also { wasUpdate = false }

        val coordinatePlayer = playerMap.coordinatePlayer

        val newCoordinatePlayer = when (command) {
            Move.UP -> Coordinate(i = coordinatePlayer.i - 1, j = coordinatePlayer.j)
            Move.DOWN -> Coordinate(i = coordinatePlayer.i + 1, j = coordinatePlayer.j)
            Move.LEFT -> Coordinate(i = coordinatePlayer.i, j = coordinatePlayer.j - 1)
            Move.RIGHT -> Coordinate(i = coordinatePlayer.i, j = coordinatePlayer.j + 1)
        }

        if (!isMovePossible(newCoordinatePlayer)) return null

        val obj = ParseMap.parse(updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j])
        wasUpdate = when (obj) {
            ParseMap.COIN, ParseMap.WEAK_COIN -> {
                player.countPoints +=  (MIN_PRICE_COIN_RANDOM..MAX_PRICE_COIN_RANDOM).random()
                updateMap.lifeCoins.removeIf { coin ->
                    coin.coordinate.i == newCoordinatePlayer.i && coin.coordinate.j == newCoordinatePlayer.j
                }
                updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j] = ParseMap.PLAYER.value
                updateMap.map[coordinatePlayer.i][coordinatePlayer.j] = ParseMap.EMPTY.value
                updateMap.coordinatePlayer = newCoordinatePlayer
                true
            }

            ParseMap.EMPTY -> {
                updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j] = ParseMap.PLAYER.value
                updateMap.map[coordinatePlayer.i][coordinatePlayer.j] = ParseMap.EMPTY.value
                updateMap.coordinatePlayer = newCoordinatePlayer
                true
            }

            else -> false
        }

        if (!wasUpdate) return null

        player.map = updateMap
        return player
    }

    fun updateMap(playerMap: PlayerMap): PlayerMap? {
        val coins = playerMap.lifeCoins
        val newCoins = mutableListOf<Coin>()
        for (coin in coins) {
            val leftTime = getLifeCoin(coin.timeCreated)
            when (leftTime) {
                in TIME_LIFE_CHANGE_COLOR_COIN_MS..TIME_LIFE_ONE_COIN_MS -> {
                    newCoins.add(coin)
                }

                in 0..TIME_LIFE_CHANGE_COLOR_COIN_MS -> {
                    playerMap.map[coin.coordinate.i][coin.coordinate.j] = ParseMap.WEAK_COIN.value
                    newCoins.add(coin)
                }

                else -> {
                    playerMap.map[coin.coordinate.i][coin.coordinate.j] = ParseMap.EMPTY.value
                }
            }
        }
        return if (coins.size != newCoins.size) {
            playerMap.lifeCoins = newCoins
            playerMap.generateCoin()
        } else {
            playerMap.maybeGenerateCoin()
        }
    }

    private fun PlayerMap.generateCoin(): PlayerMap {
        while (true) {
            val i = (0 until HEIGHT).random()
            val j = (0 until WIDTH).random()
            if (this.map[i][j] == ParseMap.EMPTY.value) {
                val currentTime = System.currentTimeMillis()
                this.lifeCoins.add(Coin(currentTime, Coordinate(i, j)))
                this.map[i][j] = ParseMap.COIN.value
                break
            }
        }
        return this
    }

    private fun PlayerMap.maybeGenerateCoin(): PlayerMap? {
        if (this.lifeCoins.isEmpty()) return this.generateCoin()
        val maxYoungCoin = this.lifeCoins.map {it.timeCreated}.max()

        if (System.currentTimeMillis() - maxYoungCoin > TIME_WHEN_CREATE_COIN)
            return this.generateCoin()

        return null
    }

    private fun getLifeCoin(createdTime: Long): Long =
        createdTime + TIME_LIFE_ONE_COIN_MS - System.currentTimeMillis()

    private fun isMovePossible(coordinate: Coordinate) =
        !(coordinate.i < 0 || coordinate.i >= HEIGHT || coordinate.j < 0 || coordinate.j >= WIDTH)

    companion object {
        private const val WIDTH = 20
        private const val HEIGHT = 15

        private const val MIN_PRICE_COIN_RANDOM = 10
        private const val MAX_PRICE_COIN_RANDOM = 50

        private val TIME_LIFE_ONE_COIN_MS = TimeUnit.SECONDS.toMillis(10)
        private val TIME_WHEN_CREATE_COIN = TimeUnit.SECONDS.toMillis(7)
        private val TIME_LIFE_CHANGE_COLOR_COIN_MS = TimeUnit.SECONDS.toMillis(5)

        enum class ParseMap(val value: Int) {
            EMPTY(0),
            PLAYER(1),
            WALL(2),
            COIN(3),
            WEAK_COIN(4);

            companion object {
                fun parse(value: Int) = when (value) {
                    0 -> EMPTY
                    1 -> PLAYER
                    2 -> WALL
                    3 -> COIN
                    4 -> WEAK_COIN
                    else -> EMPTY
                }
            }
        }
    }
}