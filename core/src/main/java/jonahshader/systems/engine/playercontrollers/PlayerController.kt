package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Piece
import jonahshader.systems.engine.Player

interface PlayerController {
    fun requestMove(id: Int, player: Player, game: BoardGame)
    fun notifyGameResult(win: Boolean)
}