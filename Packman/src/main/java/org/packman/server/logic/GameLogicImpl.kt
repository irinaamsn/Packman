package org.packman.server.logic

import java.util.concurrent.TimeUnit

class GameLogicImpl: GameLogic {

    private val mapLogic = MapLogic()
    private val usersInfo = UsersInfo
    private val db = Database

    override fun processing(ip: String, port: String, command: Command): String =
        processing(ip, port, command, null)

    override fun processing(ip: String, port: String, command: Command, name: String?): String {
        val clientAddress = ClientAddress(ip = ip, port = port)

        if (command.isRequiredExists() && !usersInfo.checkExistsPlayerByClientAddress(clientAddress)) {
            return createAnsErrorPlayerNotExists()
        }

        return when (command) {
            Command.START -> start(clientAddress, name)
            Command.FORCE_FINISH -> forceFinish(clientAddress)
            Command.UPDATE_MAP -> updateMap(clientAddress)
            Command.GET_BEST_PLAYERS -> bestPlayers()

            Command.MOVE_UP -> movePlayer(clientAddress, Move.UP)
            Command.MOVE_DOWN -> movePlayer(clientAddress, Move.DOWN)
            Command.MOVE_LEFT -> movePlayer(clientAddress, Move.LEFT)
            Command.MOVE_RIGHT -> movePlayer(clientAddress, Move.RIGHT)
        }
    }
    private fun bestPlayers(): String {
        val bestPlayersList = db.getBestPlayers()
        return createBestPlayers(bestPlayersList)
    }

    private fun start(clientAddress: ClientAddress, name: String?): String {
        val player = usersInfo.createPlayer(clientAddress, name)
        val timeLeft = getLeftTime(player)
        return createAnsStart(player.map, timeLeft)
    }

    private fun forceFinish(clientAddress: ClientAddress): String {
        val points = usersInfo.createPlayer(clientAddress)
        val currentPosition = db.getCurrentPosition(points)
        usersInfo.removePlayer(clientAddress)

        return createAnsFinish(points, currentPosition)
    }

    private fun updateMap(clientAddress: ClientAddress): String {
        val player = usersInfo.getPlayer(clientAddress)
        val timeLeft = getLeftTime(player)

        if (timeLeft <= 0) return forceFinish(clientAddress)

        val updateMap = mapLogic.updateMap(player.map) ?: return createAnsNotChanged(timeLeft)
        player.map = updateMap
        usersInfo.updatePlayer(clientAddress, player)

        return createAnsChanged(player, timeLeft)
    }

    private fun movePlayer(clientAddress: ClientAddress, command: Move): String {
        val player = usersInfo.getPlayer(clientAddress)
        val timeLeft = getLeftTime(player)

        if (timeLeft <= 0) return forceFinish(clientAddress)

        val newMapPlayer = mapLogic.movePlayer(player, command) ?: return createAnsNotChanged(timeLeft)
        usersInfo.updatePlayer(clientAddress, player)

        return createAnsChanged(newMapPlayer, timeLeft)
    }

    private fun getLeftTime(player: Player): Long {
        val currentTime = System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toSeconds(
            player.gameStartedTime + TimeUnit.SECONDS.toMillis(TOTAL_TIME_GAME_SECONDS) - currentTime
        )
    }

    private fun createAnsStart(map: PlayerMap, time: Long) = "${GameLogicAnswer.MAP} ${map.map.map{it.joinToString(",")}} $time"
    private fun createAnsErrorPlayerNotExists() = GameLogicAnswer.ERROR_GAME_NOT_EXISTS
    private fun createAnsNotChanged(time: Long) = "${GameLogicAnswer.NOT_CHANGED} $time"
    private fun createAnsChanged(player: Player, time: Long) =
        "${GameLogicAnswer.MAP} ${player.map.map.map{it.joinToString(",")}} $time ${player.countPoints}"
    private fun createAnsFinish(player: Player, currentPosition: Int) =
        "${GameLogicAnswer.FINISH_GAME} ${player.countPoints} $currentPosition"

    private fun createBestPlayers(players: List<BestPlayer>) =
        "${GameLogicAnswer.OK} $players"

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