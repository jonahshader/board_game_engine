package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.*
import kotlin.concurrent.thread

class RandomMoveAI(private val waitTime: Float): PlayerController {
    override fun requestMove(id: Int, player: Player, game: BoardGame) {
        if (waitTime == 0f) {
            val action = player.getAllMoves().random()
            game.queueMove(action.first.pos, action.second)
        } else {
            thread {
                Thread.sleep((waitTime * 1000).toLong())

                val action = player.getAllMoves().random()
                game.queueMove(action.first.pos, action.second)
            }
        }

    }

    override fun notifyGameResult(winner: Float) {
        // dont care lol
    }
}