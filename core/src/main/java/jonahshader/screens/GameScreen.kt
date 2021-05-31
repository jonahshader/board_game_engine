package jonahshader.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.BoardApp
import jonahshader.systems.engine.*
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.engine.playercontrollers.RandomMoveAI
import jonahshader.systems.math.VecInt2
import ktx.app.KtxScreen
import ktx.graphics.begin

class GameScreen : KtxScreen {
    private val boardCamera = OrthographicCamera()
    private val boardViewport: ScalingViewport
    private val game: BoardGame

    init {
        val playerControllers = mutableListOf<PlayerController>()
        playerControllers += RandomMoveAI(.00f)
        playerControllers += RandomMoveAI(.00f)
        game = BoardGame(VecInt2(8, 8), playerControllers)

        boardViewport = FitViewport(Board.TILE_SIZE * game.board.width,
            Board.TILE_SIZE * game.board.height, boardCamera)

        val kingKernel = Kernel(VecInt2(3, 3))
//        val kingKernel = Kernel(VecInt2(13, 13))
        kingKernel.fill()

        val knightKernel = Kernel(VecInt2(5, 5))
        knightKernel.addMirrored(VecInt2(1, 2), true)
        knightKernel.addMirrored(VecInt2(2, 1), true)
        knightKernel.addMirrored(VecInt2(1, -2), true)
        knightKernel.addMirrored(VecInt2(2, -1), true)

        val kingMove = Ability.makeJumpMoveAbility(kingKernel)
        val kingCapture = Ability.makeJumpCaptureAbility(kingKernel)
        val knightMove = Ability.makeJumpMoveAbility(knightKernel)
        val knightCapture = Ability.makeJumpCaptureAbility(knightKernel)
        // add some pieces
        for (y in 0 until 8) for (x in 0 until 4) {
            game.addPieceToBoard(listOf(knightMove, knightCapture), 0, "N", VecInt2(x, y))
            game.addPieceToBoard(listOf(kingMove, kingCapture), 1, "K", VecInt2(x + 4, y))
        }
//        game.addPieceToBoard(listOf(Ability.makeJumpMoveAbility(kingKernel)), 0, "K", VecInt2(2, 2))
//        game.addPieceToBoard(listOf(Ability.makeJumpMoveAbility(knightKernel)), 0, "N", VecInt2(5, 2))
//        game.addPieceToBoard(listOf(Ability.makeJumpMoveAbility(kingKernel)), 1, "K", VecInt2(8, 2))
//        game.addPieceToBoard(listOf(Ability.makeJumpMoveAbility(knightKernel)), 1, "N", VecInt2(11, 2))
        // start game
        game.startGame()
    }

    override fun render(delta: Float) {
        game.update()
        boardViewport.apply(true)
        BoardApp.batch.begin(boardCamera)
        game.draw(boardViewport)
        BoardApp.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        boardViewport.update(width, height, true)
    }
}