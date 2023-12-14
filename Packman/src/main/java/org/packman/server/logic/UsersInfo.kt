package org.packman.server.logic

object UsersInfo {
    private val players = mutableMapOf<ClientAddress, Player>()
    fun createPlayer(clientAddress: ClientAddress, name: String? = null): Player {
        val mapForPlayer = MapLogic().createMap()
        val currentTime = System.currentTimeMillis()
        val player = Player(
            name = name ?: Player.DEFAULT_NAME,
            gameStartedTime = currentTime,
            countPoints = STARTED_PINTS,
            map =  mapForPlayer,
        )

        players[clientAddress] = player

        return player
    }

    fun getPlayer(clientAddress: ClientAddress): Player = players[clientAddress] ?: createPlayer(clientAddress)
    fun removePlayer(clientAddress: ClientAddress) = players.remove(clientAddress)
    fun updatePlayer(clientAddress: ClientAddress, player: Player) {
        players[clientAddress] = player
    }
    fun checkExistsPlayerByClientAddress(clientAddress: ClientAddress): Boolean = players.containsKey(clientAddress)

    private const val STARTED_PINTS = 0
}