package org.packman.server.logic

enum class Command {
    START,
    UPDATE_MAP,
    FORCE_FINISH,
    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,
    GET_BEST_PLAYERS
}

enum class Move {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

