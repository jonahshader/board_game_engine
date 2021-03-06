package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RandomMoveEvalAI(private val playerId: Int, private val evalsPerTurn: Int, private val maxDepth: Int, private val subAi0: PlayerController = RandomMoveAI(0f), private val subAi1: PlayerController = RandomMoveAI(0f)): PlayerController {
    override fun requestMove(id: Int, player: Player, game: BoardGame) {
        GlobalScope.launch {
            val boards = HashMap<Move, MutableList<BoardGame>>()
            val moveValues = HashMap<Move, Float>()
            for (i in 0 until evalsPerTurn) {
                val move = game.getCurrentPlayer().getAllMoves().random()
                moveValues[move] = 0f
                if (!boards.containsKey(move)) {
                    boards[move] = mutableListOf()
//
                }
                boards[move]!! += BoardGame(game, listOf(subAi0, subAi1))
            }
            boards.keys.parallelStream().forEach { key ->
                val value = boards[key]
                value!!.forEach {
                    it.queueMove(key.first.pos, key.second)
                    val gameResult = it.finishGame(maxDepth)
                    val currentMoveValue = moveValues[key]!!
//                    if (gameResult < 0) {
////                        moveValues[key] = currentMoveValue + .33f / it.totalMoves
//                    } else if (gameResult == playerId) {
//                        moveValues[key] = currentMoveValue + 1f / it.totalMoves
//                    }
                    if (gameResult >= 0) {
                        if (gameResult == playerId) moveValues[key] = currentMoveValue + 1f / (it.totalMoves * boards[key]!!.size)
                        else moveValues[key] = currentMoveValue - 2f / (it.totalMoves * boards[key]!!.size)
                    }
                }
            }
//            for (g in boards) {
//                g.value.forEach {
//                    it.queueMove(g.key.first.pos, g.key.second)
//                    val gameResult = it.finishGame()
//                    if (gameResult < 0) {
//                        moveValues[g.key] = moveValues[g.key]!! + .33f
//                    } else if (gameResult == playerId) {
//                        moveValues[g.key] = moveValues[g.key]!! + 1f
//                    }
//                }
//            }
            // find max
            var bestMove = moveValues.keys.first()
            var bestMoveVal = -100000000f
            for (k in moveValues.keys) {
                if (moveValues[k]!! > bestMoveVal) {
                    bestMoveVal = moveValues[k]!!
                    bestMove = k
                }
            }

//            println("best move is $bestMove with a score of $bestMoveVal")

            // make move
            game.queueMove(bestMove.first.pos, bestMove.second)
        }
    }

    override fun notifyGameResult(win: Float) {

    }
}