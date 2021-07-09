package jonahshader.systems.engine.playercontrollers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import jonahshader.systems.engine.Board
import jonahshader.systems.engine.BoardGame
import jonahshader.systems.engine.Piece
import jonahshader.systems.engine.Player
import jonahshader.systems.math.VecInt2
import ktx.app.KtxInputAdapter

class LocalHumanController(private val playerId: Int): PlayerController, KtxInputAdapter {
    private var selectedPiece: Piece? = null
    private var game: BoardGame? = null
    private var moveRequested = false
    var viewport: Viewport? = null

    init {
        Gdx.input.inputProcessor = this
    }

    override fun requestMove(id: Int, player: Player, game: BoardGame) {
        this.game = game
        moveRequested = true
    }

    override fun notifyGameResult(win: Float) {

    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (viewport == null || !moveRequested) return false
        val posVec = viewport!!.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        posVec.x /= Board.TILE_SIZE
        posVec.y /= Board.TILE_SIZE
        val pos = VecInt2(posVec)
        if (game != null) {
            if (game!!.board.posIsOnBoard(pos)) {
                val piece = game!!.board.getPiece(pos)
                if (piece != null) {
                    if (piece.ownerID == playerId) {
                        piece.select()
                        selectedPiece = piece
                    }
                }
                return true
            }
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (viewport == null || !moveRequested) return false
        val posVec = viewport!!.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        posVec.x /= Board.TILE_SIZE
        posVec.y /= Board.TILE_SIZE
        val pos = VecInt2(posVec)
        if (game != null && selectedPiece != null) {
            if (game!!.board.posIsOnBoard(pos)) {
                val moves = selectedPiece!!.getAllValidActionPos(game!!)
                if (moves.contains(pos)) {
                    // make move
                    game!!.queueMove(selectedPiece!!.pos, pos)
                    moveRequested = false
                }
                selectedPiece?.unselect()
                selectedPiece = null
                return true
            }
        }
        selectedPiece?.unselect()
        selectedPiece = null
        return false
    }
}