package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2
import java.util.*
import java.util.concurrent.atomic.AtomicReference

// manages a game. contains players and board. keeps track of who's turn it is and makes move requests
class BoardGame {
    val board: Board
    val players = mutableListOf<Player>()
    private var playerTurn = 0
    private val moveQueue = Collections.synchronizedList(ArrayList<Pair<VecInt2, VecInt2>>())
    private val moveTime: Float
    var loseCondition: LoseCondition
    var drawCondition: DrawCondition


    private var winner = -1
    private var gameOver = false

    constructor(size: VecInt2, controllers: List<PlayerController>, moveTime: Float = .25f,
    loseCondition: LoseCondition = missingAllPiecesLoseCondition,
    drawCondition: DrawCondition = noMoveDraw) {
        this.moveTime = moveTime
        this.loseCondition = loseCondition
        this.drawCondition = drawCondition
        board = Board(size)
        controllers.forEachIndexed {index, controller -> players += Player(index, controller, this, when(index) {
            0 -> Color(.3f, .9f, .9f, 1f)
            1 -> Color(.6f, .1f, .1f, 1f)
            else -> Color(.5f, .5f, .5f, 1f)
        })}
    }

    // copy constructor for ai
    constructor(toCopy: BoardGame, newControllers: List<PlayerController>) {
        moveTime = 0f
        loseCondition = toCopy.loseCondition
        drawCondition = toCopy.drawCondition
        playerTurn = toCopy.playerTurn
        board = Board(toCopy.board)
        newControllers.forEachIndexed {index, controller -> players += Player(index, controller, this, when(index) {
            0 -> Color(.3f, .9f, .9f, 1f)
            1 -> Color(.6f, .1f, .1f, 1f)
            else -> Color(.5f, .5f, .5f, 1f)
        })}
    }



    fun startGame() {
        players[0].requestMove()
    }


    fun queueMove(piecePos: VecInt2, movePos: VecInt2) {
        synchronized(moveQueue) {
            moveQueue.add(Pair(piecePos, movePos))
        }
    }

    private fun makeMove(piecePos: VecInt2, movePos: VecInt2) {
        board.getPiece(piecePos)!!.makeMove(movePos, this)
        playerTurn++
        if (playerTurn >= players.size) playerTurn -= players.size
    }

    fun addPieceToBoard(abilities: MutableList<Ability>, playerID: Int, symbol: String, pos: VecInt2): Piece {
        val piece = Piece(abilities, playerID, symbol, players[playerID].color, pos)
        players[piece.ownerID].addPiece(piece)
        board.addPiece(piece)
        return piece
    }

    fun capturePiece(piece: Piece, to: VecInt2) {
        val toCapture = board.getPiece(to)!!
        players[toCapture.ownerID].removePiece(toCapture)
        board.movePiece(piece, to)
    }

    fun draw(viewport: ScalingViewport, dt: Float) {
        // draw board
        board.draw()

        // draw pieces
        players.forEach { it.draw(viewport, dt, moveTime) }
    }

    fun update() {
        assert(moveQueue.size <= 1) { "SHIT. theres ${moveQueue.size} things in that queue" }
        var queueNextMove = false
        synchronized(moveQueue) {
            if (!gameOver && moveQueue.size == 1) {
//            moveQueue.forEach {
                val it = moveQueue[0]
                makeMove(it.first, it.second)
                checkLose()
                checkWin()
                checkDraw()
                if (gameOver) {
                    moveQueue.clear()
                    if (winner == 1)
                        println("PlayerID $winner won!")
                    players.forEach { it.notifyGameOver(winner) }

                    return@update
                }
                queueNextMove = true
            }
            moveQueue.clear()
        }

        if (!gameOver && queueNextMove) {
            players[playerTurn].requestMove()
        }

    }

    fun finishGame(): Int {
        while (!gameOver) {
            update()
        }
        return winner
    }

    private fun checkDraw() {
        if (drawCondition(this)) {
            gameOver = true
        }
    }

    private fun checkLose() {
        val loser = loseCondition(this)
        if (loser >=0) {
            players[loser].lost = true
        }
    }

    private fun checkWin() {
        var winnerID = -1
        var nonLostCount = 0
        players.forEach{
            if (!it.lost) {
                winnerID = it.id
                nonLostCount++
            }
        }

        if (nonLostCount == 1) {
            winner = winnerID
            gameOver = true
        }
    }

    fun getCurrentPlayer(): Player {
        return players[playerTurn]
    }
}