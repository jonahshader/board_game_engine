package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RandomMoveAI(private val waitTime: Float): PlayerController {
    override fun requestMove(id: Int, player: Player, game: BoardGame) {
        if (waitTime == 0f) {
            val action = player.getAllMoves().random()
            game.queueMove(action.first.pos, action.second)
        } else {
            GlobalScope.launch {
                delay((waitTime * 1000).toLong())

                val action = player.getAllMoves().random()
                game.queueMove(action.first.pos, action.second)

            }
        }
    }


    override fun notifyGameResult(winner: Float) {
        // dont care lol
    }
}