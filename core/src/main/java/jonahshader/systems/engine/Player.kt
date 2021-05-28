package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.systems.engine.playercontrollers.PlayerController

class Player(private val id: Int, private val controller: PlayerController, private val game: BoardGame, val color: Color) {
    private val rawPieces: MutableList<Piece> = mutableListOf()
    val pieces: List<Piece>
        get() = rawPieces

    fun requestMove() {
        controller.requestMove(id, pieces, game)
    }

    fun draw(viewport: ScalingViewport) {
//        pieces.forEach{it.drawValidMoveTiles(game.board)}
        pieces.forEach{it.draw(viewport)}
    }

    fun addPiece(piece: Piece) {
        rawPieces += piece
    }
}