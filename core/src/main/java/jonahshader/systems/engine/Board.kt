package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import jonahshader.BoardApp
import jonahshader.systems.math.VecInt2

class Board(private val size: VecInt2) {
    companion object {
        const val TILE_SIZE = 16f
    }
    val width: Int
        get() = size.x
    val height: Int
        get() = size.y

    private val lightTileColor = Color(.8f, .8f, .8f, 1f)
    private val darkTileColor = Color(.2f, .2f, .2f, 1f)

    private val board = mutableListOf<Piece?>()

    init {
        for (i in 0 until size.x * size.y)
            board += null
    }

    fun posToIndex(pos: VecInt2) = pos.x + pos.y * size.x
    fun getPiece(pos: VecInt2) = if (posIsOnBoard(pos)) board[posToIndex(pos)] else null
    fun posIsOnBoard(pos: VecInt2) = pos.x in 0 until width && pos.y in 0 until height
    fun setPiece(pos: VecInt2, piece: Piece?) { board[posToIndex(pos)] = piece}
    fun addPiece(piece: Piece) { board[posToIndex(piece.pos)] = piece }
    fun movePiece(piece: Piece, to: VecInt2) {
        setPiece(to, piece)
        setPiece(piece.pos, null)
        piece.move(to)
    }

    fun draw() {
        for (y in 0 until height) for (x in 0 until width) {
            BoardApp.shapeDrawer.filledRectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, if ((x + y) % 2 == 0) darkTileColor else lightTileColor)
        }
    }
}