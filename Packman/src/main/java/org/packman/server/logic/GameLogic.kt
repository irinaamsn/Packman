package org.packman.server.logic

interface GameLogic {

    /**
	 * Входной метод для использования логической части игры
	 *
	 * Входные параметры:
	 *  ip тип [String] - передается для определения айпи клиента
	 *  port тип [String] - передается для определения порта клиента
	 *  command тип enum класс [Command] - определяет ход действия в логической части
	 *  name тип [String] - имя игрока, используется только при передачи [Command.START],
	 *  чтобы определить имя игрока для сохранения истории результатов игр. Можно не передавать имя игрока, тогда
	 *  будет использоваться имя по умолчанию
	 *
	 * Возможные ответы
	 *  При переданном:
	 *  1)  [Command.START]:
	 *  		"MAP {int[][]} {time}"
	 *  2)  [Command.UPDATE_MAP]
	 *  	[Command.MOVE_UP],
	 *  	[Command.MOVE_LEFT],
	 *  	[Command.MOVE_RIGHT],
	 *  	[Command.UPDATE_MAP]:
	 *  		“MAP {int[][]} {timeLeft} {currentPoints}” - были изменения на карте
	 *  		“NOT_CHANGED {timeLeft}” - изменений по карте не было
	 *  		“FINISH_GAME {currentPoints} {currentPosition}” - время игры закончилось
	 *  		“ERROR_GAME_NOT_EXISTS” - игрок с данным аайпи и портом не найден в текущей сессии
	 *  3) 	[Command.FORCE_FINISH]:
	 *  		“FINISH_GAME {currentPoints} {currentPosition}” - игры была успешно окончена
	 * 			“ERROR_GAME_NOT_EXISTS” - игрок с данным аайпи и портом не найден в текущей сессии
	 * 	4) 	[Command.GET_BEST_PLAYERS]:
	 * 			"OK {$name,$countPoints;$name,$countPoints;$name,$countPoints;}"
    **/
    fun processing(ip: String, port: String, command: Command, name: String?): String

	fun processing(ip: String, port: String, command: Command): String
}