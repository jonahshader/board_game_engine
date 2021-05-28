package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.*
import kotlin.concurrent.thread

class RandomMoveAI(private val waitTime: Float): PlayerController {
    override fun requestMove(id: Int, pieces: List<Piece>, game: BoardGame) {
        thread {
            Thread.sleep((waitTime * 500).toLong())
            val pieceActionPairs = mutableListOf<Pair<Piece, ActionPos>>()
            pieces.forEach {
                it.getAllValidActionPos(game.board).forEach { actionPos ->
                    pieceActionPairs += Pair(it, actionPos)
                }
            }

            Thread.sleep((waitTime * 500).toLong())
            val action = pieceActionPairs.random()
            game.makeMove(action.first.pos, action.second)
        }
    }

    override fun notifyGameResult(win: Boolean) {
        // dont care lol
    }
}