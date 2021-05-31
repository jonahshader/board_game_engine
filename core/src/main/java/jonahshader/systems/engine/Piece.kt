package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.BoardApp
import jonahshader.systems.math.VecInt2
import jonahshader.systems.ui.CustomShapes
import jonahshader.systems.ui.TextRenderer

data class Piece(private val abilities: List<Ability>, val ownerID: Int, private val symbol: String, private val bodyColor: Color, val pos: VecInt2) {
    private val roundness = Board.TILE_SIZE / 6
    private val padding = Board.TILE_SIZE / 12
    private val shadowDist = Board.TILE_SIZE / 16
    private val shadowOpacity = .33f

    private val textColor = Color(1f, 1f, 1f, 1f)

    init {
        textColor.r = textColor.r * .5f + bodyColor.r * .5f
        textColor.g = textColor.g * .5f + bodyColor.g * .5f
        textColor.b = textColor.b * .5f + bodyColor.b * .5f
    }

    fun isOnSameTeam(otherPiece: Piece) = ownerID == otherPiece.ownerID
    fun getAllSpecificActions(game: BoardGame): List<SpecificAction> {
        val actions = mutableListOf<SpecificAction>()
        abilities.forEach { ability ->
            ability.getAllValidActionPos(game, this).forEach { actionPos ->
                actions += ability.makeSpecificAction(game, actionPos, this)
            }
        }
        return actions
    }

    fun getAllValidActionPos(game: BoardGame): List<ActionPos> {
        val actions = mutableListOf<ActionPos>()
        abilities.forEach { ability ->
            actions += ability.getAllValidActionPos(game, this)
        }
        return actions
    }

//    fun getAllActionPositions()

    fun makeMove(movePos: VecInt2, game: BoardGame) {
        // figure out which ability contains a valid move at movePos
        for (a in abilities) {
            if (a.actionValid(game, movePos, this)) {
                a.action(game, movePos, this)
                break
            }
        }
    }

    fun draw(viewport: ScalingViewport) {
        val x = pos.x * Board.TILE_SIZE
        val y = pos.y * Board.TILE_SIZE
        BoardApp.shapeDrawer.setColor(0f, 0f, 0f, shadowOpacity)
        CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding - shadowDist, y + padding - shadowDist, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        BoardApp.shapeDrawer.setColor(bodyColor)
        CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding, y + padding, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        TextRenderer.begin(BoardApp.batch, viewport, TextRenderer.Font.HEAVY, Board.TILE_SIZE, 0f)
        TextRenderer.color = textColor
        TextRenderer.drawTextCentered(x + Board.TILE_SIZE / 2f, y + Board.TILE_SIZE / 2f, symbol, Board.TILE_SIZE / 32, .33f)
        TextRenderer.end()
    }

    fun drawValidMoveTiles(game: BoardGame) {
        BoardApp.shapeDrawer.setColor(1f, 1f, 0f, 1f)
        getAllValidActionPos(game).forEach {
            val x = it.x * Board.TILE_SIZE
            val y = it.y * Board.TILE_SIZE
            CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding, y + padding, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        }
    }
}