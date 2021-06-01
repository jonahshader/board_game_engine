package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2

class Player(val id: Int, private val controller: PlayerController, private val game: BoardGame, val color: Color) {
    private val rawPieces: MutableList<Piece> = mutableListOf()
    val pieces: List<Piece>
        get() = rawPieces

    var lost = false

    fun requestMove() {
        controller.requestMove(id, this, game)
    }

    fun getAllMoves(): List<Pair<Piece, ActionPos>> {
        val pieceActionPairs = mutableListOf<Pair<Piece, ActionPos>>()
        pieces.forEach {
            it.getAllValidActionPos(game).forEach { actionPos ->
                pieceActionPairs += Pair(it, actionPos)
            }
        }

        return pieceActionPairs
    }

    fun draw(viewport: ScalingViewport) {
//        pieces.forEach{it.drawValidMoveTiles(game.board)}
        pieces.forEach{it.draw(viewport)}
    }

    fun addPiece(piece: Piece) {
        rawPieces += piece
    }

    fun notifyGameOver(winner: Int) {
        if (winner >= 0) {
            controller.notifyGameResult(if(winner == id) 1f else 0f)
        } else {
            controller.notifyGameResult(.5f) // tie
        }

    }

    fun removePiece(toRemove: Piece) {
        rawPieces -= toRemove
    }
}