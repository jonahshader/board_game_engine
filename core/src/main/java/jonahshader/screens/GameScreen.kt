package jonahshader.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.BoardApp
import jonahshader.systems.engine.*
import jonahshader.systems.engine.games.makeChessGame
import jonahshader.systems.engine.games.makeGardnerChessGame
import jonahshader.systems.engine.playercontrollers.*
import jonahshader.systems.math.VecInt2
import ktx.app.KtxScreen
import ktx.graphics.begin

class GameScreen : KtxScreen {
    private val boardCamera = OrthographicCamera()
    private var boardViewport: ScalingViewport? = null
    private val game: BoardGame

    init {
        val playerControllers = mutableListOf<PlayerController>()
//        playerControllers += MaterialAI(0, 11920, 5)
        playerControllers += MiniMaxAI(6)
//        val humanPlayer = LocalHumanController(0)
//        val humanPlayer2 = LocalHumanController(1)
//        playerControllers += humanPlayer
//        playerControllers += humanPlayer2
//        playerControllers += RandomMoveEvalAI(0, 32, 10, RandomMoveEvalAI(0, 50, 10), RandomMoveEvalAI(1, 50, 10))
        playerControllers += MiniMaxAI(6)
//        playerControllers += MaterialAI(1, 11920, 6)
//        playerControllers += RandomMoveAI(1f)
//        playerControllers += RandomMoveAI(.15f)
//        game = makeChessGame(playerControllers[0], playerControllers[1], .5f, 1500)
        game = makeGardnerChessGame(playerControllers[0], playerControllers[1], .5f, 1500)

        boardViewport = FitViewport(Board.TILE_SIZE * game.board.width,
            Board.TILE_SIZE * game.board.height, boardCamera)
//        humanPlayer.viewport = boardViewport
//        humanPlayer2.viewport = boardViewport
        game.startGame()
    }

    override fun render(delta: Float) {
        game.update()
        boardViewport!!.apply(true)
        BoardApp.batch.begin(boardCamera)
        game.draw(boardViewport!!, delta)
        BoardApp.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        boardViewport!!.update(width, height, true)
    }
}