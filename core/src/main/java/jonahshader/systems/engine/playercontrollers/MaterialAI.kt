package jonahshader.systems.engine.playercontrollers

import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Move
import jonahshader.systems.engine.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MaterialAI(private val playerId: Int, private val evalsPerTurn: Int, private val depth: Int,  private val subAi0: PlayerController = RandomMoveAI(0f), private val subAi1: PlayerController = RandomMoveAI(0f)): PlayerController {
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
                    var lastDepth = 1
                    loop@for (i in 1..depth) {
                        it.update()
                        lastDepth = i
                        if (it.gameOver) break@loop
                    }
                    var stateMaterialDifference = it.evalMaterialDifference(playerId)
                    if (stateMaterialDifference < 0) stateMaterialDifference *= 2
                    val currentMoveValue = moveValues[key]!!

                    moveValues[key] = currentMoveValue + stateMaterialDifference / (lastDepth * boards[key]!!.size)
                }
            }

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