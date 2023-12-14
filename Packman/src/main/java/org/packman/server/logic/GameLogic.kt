package org.packman.server.logic

import java.util.concurrent.TimeUnit

class GameLogic {

    private val mapLogic = MapLogic()
    private val usersInfo = UsersInfo
    private val db = Database

    fun processing(ip: String, port: String, command: Command, name: String? = null): String {
        val clientAddress = ClientAddress(ip = ip, port = port)

        if (command.isRequiredExists() && !usersInfo.checkExistsPlayerByClientAddress(clientAddress)) {
            // @TODO(Adel Grishin) make parse error
            return "ERROR"
        }

        return when (command) {
            Command.START -> start(clientAddress, name)
            Command.FORCE_FINISH -> start(clientAddress, name)
            Command.UPDATE_MAP -> updateMap(clientAddress)

            Command.MOVE_UP -> movePlayer(clientAddress, Move.UP)
            Command.MOVE_DOWN -> movePlayer(clientAddress, Move.DOWN)
            Command.MOVE_LEFT -> movePlayer(clientAddress, Move.LEFT)
            Command.MOVE_RIGHT -> movePlayer(clientAddress, Move.RIGHT)
        }
    }

    private fun start(clientAddress: ClientAddress, name: String?): String {
        val player = usersInfo.createPlayer(clientAddress, name)
        val map = player.map
        // @TODO (Adel Grishin) Make parsing map and time for socket
        return "$map, $TOTAL_TIME_GAME_SECONDS"
    }

    private fun forceFinish(clientAddress: ClientAddress): String {
        val points = usersInfo.createPlayer(clientAddress)
        val currentPosition = db.getCurrentPosition()
        usersInfo.removePlayer(clientAddress)
        return "$points $currentPosition"
    }

    private fun updateMap(clientAddress: ClientAddress): String {
        val player = usersInfo.getPlayer(clientAddress)
        val timeLeft = getLeftTime(player)

        if (timeLeft <= 0) return forceFinish(clientAddress)

        val updateMap = mapLogic.updateMap(player.map)
        // @TODO (Adel Grishin) Make parsing map and time for socket
        return if (updateMap == null) "null $timeLeft" else "${updateMap.map} $timeLeft ${updateMap.lifeCoins}"
    }

    private fun getLeftTime(player: Player): Long {
        val currentTime = System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toSeconds(
            player.gameStartedTime + TimeUnit.SECONDS.toMillis(TOTAL_TIME_GAME_SECONDS) - currentTime
        )
    }

    private fun movePlayer(clientAddress: ClientAddress, command: Move): String {
        val player = usersInfo.getPlayer(clientAddress)
        val timeLeft = getLeftTime(player)

        if (timeLeft <= 0) return forceFinish(clientAddress)

        val newMapPlayer = mapLogic.movePlayer(player, command)
        // @TODO (Adel Grishin) Make parsing map and time for socket
        return if (newMapPlayer == null) "null $timeLeft" else "${newMapPlayer.map} $timeLeft ${newMapPlayer.lifeCoins}"
    }

    private fun Command.isRequiredExists() = when (this) {
        Command.UPDATE_MAP,
        Command.FORCE_FINISH,
        Command.MOVE_UP,
        Command.MOVE_DOWN,
        Command.MOVE_LEFT,
        Command.MOVE_RIGHT -> true

        else -> false
    }

    companion object {
        private const val TOTAL_TIME_GAME_SECONDS = 59L
    }
}