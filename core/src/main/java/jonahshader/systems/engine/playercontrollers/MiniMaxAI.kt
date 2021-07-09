package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Move
import jonahshader.systems.engine.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.lang.Float.min
import kotlin.system.measureTimeMillis

class MiniMaxAI(private val depth: Int): PlayerController {
    companion object {
        private val emptyController = EmptyController()
        private val emptyControllerList = listOf(emptyController, emptyController)
    }

    fun minimax(position: BoardGame, alpha: Float, beta: Float, depth: Int): Pair<Move?, Float> {
        var a = alpha
        var b = beta

        if (depth == 0 || position.gameOver) {
            val eval = position.evalMaterialDifference()
//            println(eval)
            return Pair(null, eval)
        }
        val moves = position.getCurrentPlayer().getAllMoves()
        var bestMove = moves.random()

        if (position.playerTurn == 0) {
            var maxEval = Float.NEGATIVE_INFINITY
            for (it in moves) {
                val newState = BoardGame(position, emptyControllerList)
                newState.queueMove(it)
                newState.updateNoMoveRequest()
                val eval = minimax(newState, a, b, depth - 1).second
                if (eval > maxEval) {
                    maxEval = eval
                    bestMove = it
                }
                a = max(a, eval)
                if (b <= a)
                    break
            }
            return Pair(bestMove, maxEval)
        } else {
            var minEval = Float.POSITIVE_INFINITY
            for (it in moves) {
                val newState = BoardGame(position, emptyControllerList)
                newState.queueMove(it)
                newState.updateNoMoveRequest()
                val eval = minimax(newState, a, b, depth - 1).second
                if (eval < minEval) {
                    minEval = eval
                    bestMove = it
                }
                b = min(b, eval)
                if (b <= a)
                    break
            }
            return Pair(bestMove, minEval)
        }
    }

    override fun requestMove(id: Int, player: Player, game: BoardGame) {
        GlobalScope.launch {
            val move: Move
            val time = measureTimeMillis {
                move = minimax(game, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, depth).first!!
            }

//            delay(max(0f, 1000f-time).toLong())

            game.queueMove(move.first.pos, move.second)
        }
    }

    override fun notifyGameResult(win: Float) {

    }
}