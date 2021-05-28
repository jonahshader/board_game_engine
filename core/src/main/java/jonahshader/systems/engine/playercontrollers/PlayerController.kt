package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Piece

interface PlayerController {
    fun requestMove(id: Int, pieces: List<Piece>, game: BoardGame)
    fun notifyGameResult(win: Boolean)
}