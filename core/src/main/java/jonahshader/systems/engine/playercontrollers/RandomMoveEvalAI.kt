package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RandomMoveEvalAI(private val playerId: Int): PlayerController {
    private val evalsPerTurn = 1024
    private val randomMoveAi = RandomMoveAI(0f)
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
                boards[move]!! += BoardGame(game, listOf(randomMoveAi, randomMoveAi))
            }
            for (g in boards) {
                g.value.forEach {
                    it.queueMove(g.key.first.pos, g.key.second)
                    val gameResult = it.finishGame()
                    if (gameResult < 0) {
                        moveValues[g.key] = moveValues[g.key]!! + .5f
                    } else if (gameResult == playerId) {
                        moveValues[g.key] = moveValues[g.key]!! + .5f
                    }
                }
            }
            // find max
            var bestMove = moveValues.keys.first()
            var bestMoveVal = -1f
            for (k in moveValues.keys) {
                if (moveValues[k]!! > bestMoveVal) {
                    bestMoveVal = moveValues[k]!!
                    bestMove = k
                }
            }

            // make move
            game.queueMove(bestMove.first.pos, bestMove.second)
        }
    }

    override fun notifyGameResult(win: Float) {

    }
}