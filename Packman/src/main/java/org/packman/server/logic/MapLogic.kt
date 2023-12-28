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
            ParseMap.COIN_LOW, ParseMap.WEAK_COIN_LOW -> {
                val addPoints = (MIN_PRICE_COIN_LOW_RANDOM..MAX_PRICE_COIN_LOW_RANDOM).random()
                eatCoin(player, updateMap, newCoordinatePlayer, coordinatePlayer, addPoints)
            }

            ParseMap.COIN_MIDDLE, ParseMap.WEAK_COIN_MIDDLE -> {
                val addPoints = (MIN_PRICE_COIN_MIDDLE_RANDOM..MAX_PRICE_COIN_MIDDLE_RANDOM).random()
                eatCoin(player, updateMap, newCoordinatePlayer, coordinatePlayer, addPoints)
            }

            ParseMap.COIN_POWERFUL, ParseMap.WEAK_COIN_POWERFUL -> {
                val addPoints = (MIN_PRICE_COIN_POWERFUL_RANDOM..MAX_PRICE_COIN_POWERFUL_RANDOM).random()
                eatCoin(player, updateMap, newCoordinatePlayer, coordinatePlayer, addPoints)
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

    private fun eatCoin(
        player: Player,
        updateMap: PlayerMap,
        newCoordinatePlayer: Coordinate,
        coordinatePlayer: Coordinate,
        addPoints: Int,
    ): Boolean {
        player.countPoints += addPoints
        updateMap.lifeCoins.removeIf { coin ->
            coin.coordinate.i == newCoordinatePlayer.i && coin.coordinate.j == newCoordinatePlayer.j
        }
        updateMap.map[newCoordinatePlayer.i][newCoordinatePlayer.j] = ParseMap.PLAYER.value
        updateMap.map[coordinatePlayer.i][coordinatePlayer.j] = ParseMap.EMPTY.value
        updateMap.coordinatePlayer = newCoordinatePlayer
        return true
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
                    playerMap.map[coin.coordinate.i][coin.coordinate.j] = ParseMap.WEAK_COIN_LOW.value
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
        val coin = when((0..10)) {
            0..1 -> ParseMap.COIN_POWERFUL
            2..3 -> ParseMap.COIN_MIDDLE
            else -> ParseMap.COIN_LOW
        }
        repeat(100) {
            val i = (0 until HEIGHT).random()
            val j = (0 until WIDTH).random()
            if (this.map[i][j] == ParseMap.EMPTY.value) {
                val currentTime = System.currentTimeMillis()
                this.lifeCoins.add(Coin(currentTime, Coordinate(i, j)))
                this.map[i][j] = coin.value
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

        private const val MIN_PRICE_COIN_LOW_RANDOM = 30
        private const val MAX_PRICE_COIN_LOW_RANDOM = 50

        private const val MIN_PRICE_COIN_MIDDLE_RANDOM = 50
        private const val MAX_PRICE_COIN_MIDDLE_RANDOM = 70

        private const val MIN_PRICE_COIN_POWERFUL_RANDOM = 70
        private const val MAX_PRICE_COIN_POWERFUL_RANDOM = 120

        private val TIME_LIFE_ONE_COIN_MS = TimeUnit.SECONDS.toMillis(12)
        private val TIME_WHEN_CREATE_COIN = TimeUnit.SECONDS.toMillis(7)
        private val TIME_LIFE_CHANGE_COLOR_COIN_MS = TimeUnit.SECONDS.toMillis(5)

        enum class ParseMap(val value: Int) {
            EMPTY(0),
            PLAYER(1),
            WALL(2),
            COIN_LOW(3),
            WEAK_COIN_LOW(4),
            COIN_MIDDLE(5),
            WEAK_COIN_MIDDLE(6),
            COIN_POWERFUL(7),
            WEAK_COIN_POWERFUL(8);

            companion object {
                fun parse(value: Int) = when (value) {
                    0 -> EMPTY
                    1 -> PLAYER
                    2 -> WALL
                    3 -> COIN_LOW
                    4 -> WEAK_COIN_LOW
                    5 -> COIN_MIDDLE
                    6 -> WEAK_COIN_MIDDLE
                    7 -> COIN_POWERFUL
                    8 -> WEAK_COIN_POWERFUL
                    else -> EMPTY
                }
            }
        }
    }
}