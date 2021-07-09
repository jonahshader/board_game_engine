package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Player

class EmptyController: PlayerController {
    override fun requestMove(id: Int, player: Player, game: BoardGame) {}
    override fun notifyGameResult(win: Float) {}
}