package jonahshader.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.BoardApp
import jonahshader.systems.engine.*
import jonahshader.systems.engine.games.makeChessGame
import jonahshader.systems.engine.games.makeGardnerChessGame
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.engine.playercontrollers.RandomMoveAI
import jonahshader.systems.engine.playercontrollers.RandomMoveEvalAI
import jonahshader.systems.math.VecInt2
import ktx.app.KtxScreen
import ktx.graphics.begin

class GameScreen : KtxScreen {
    private val boardCamera = OrthographicCamera()
    private val boardViewport: ScalingViewport
    private val game: BoardGame

    init {
        val playerControllers = mutableListOf<PlayerController>()
        playerControllers += RandomMoveEvalAI(0, 8192 * 4)
        playerControllers += RandomMoveAI(1f)
        game = makeGardnerChessGame(playerControllers[0], playerControllers[1], .5f)

        boardViewport = FitViewport(Board.TILE_SIZE * game.board.width,
            Board.TILE_SIZE * game.board.height, boardCamera)
        game.startGame()
    }

    override fun render(delta: Float) {
        game.update()
        boardViewport.apply(true)
        BoardApp.batch.begin(boardCamera)
        game.draw(boardViewport, delta)
        BoardApp.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        boardViewport.update(width, height, true)
    }
}