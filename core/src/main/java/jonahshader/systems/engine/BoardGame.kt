package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2

// manages a game. contains players and board. keeps track of who's turn it is and makes move requests
class BoardGame(size: VecInt2, controllers: List<PlayerController>) {
    val board = Board(size)
    private val players = mutableListOf<Player>()
    private var playerTurn = 0

    init {
        controllers.forEachIndexed {index, controller -> players += Player(index, controller, this, when(index) {
            0 -> Color(.3f, .9f, .9f, 1f)
            1 -> Color(.6f, .1f, .1f, 1f)
            else -> Color(.5f, .5f, .5f, 1f)
        })}
    }

    fun startGame() {
        players[0].requestMove()
    }


    fun makeMove(piecePos: VecInt2, movePos: VecInt2) {
        board.getPiece(piecePos)!!.makeMove(movePos, board)
        playerTurn++
        if (playerTurn >= players.size) playerTurn -= players.size
        players[playerTurn].requestMove()
    }

    fun addPieceToBoard(abilities: List<Ability>, playerID: Int, symbol: String, pos: VecInt2) {
        val piece = Piece(abilities, playerID, symbol, players[playerID].color, pos)
        players[piece.ownerID].addPiece(piece)
        board.addPiece(piece)
    }

    fun draw(viewport: ScalingViewport) {
        // draw board
        board.draw()

        // draw pieces
        players.forEach { it.draw(viewport) }
    }
}