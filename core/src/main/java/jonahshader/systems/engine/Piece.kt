package jonahshader.systems.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.BoardApp
import jonahshader.systems.math.VecInt2
import jonahshader.systems.ui.CustomShapes
import jonahshader.systems.ui.TextRenderer
import ktx.graphics.copy

class Piece{
    private val roundness = Board.TILE_SIZE / 6
    private val padding = Board.TILE_SIZE / 12
    private val shadowDist = Board.TILE_SIZE / 16
    private val shadowOpacity = .33f

    private val pPos: VecInt2
    private var moveProgress = 0f

    private val textColor = Color(1f, 1f, 1f, 1f)
    private var selected = false

    val abilities: MutableList<Ability>
    val ownerID: Int
    var typeID: Int
    var symbol: String
    var value: Float
    private val bodyColor: Color
    val pos: VecInt2

    constructor(    abilities: MutableList<Ability>,
                    ownerID: Int,
                    typeID: Int,
                    symbol: String,
                    value: Float,
                    bodyColor: Color,
                    pos: VecInt2) {
        this.abilities = abilities
        this.ownerID = ownerID
        this.typeID = typeID
        this.symbol = symbol
        this.value = value
        this.bodyColor = bodyColor
        this.pos = pos
        pPos = VecInt2(pos)

        textColor.r = textColor.r * .5f + bodyColor.r * .5f
        textColor.g = textColor.g * .5f + bodyColor.g * .5f
        textColor.b = textColor.b * .5f + bodyColor.b * .5f
    }

    // copy constructor
    constructor(toCopy: Piece) {
        abilities = mutableListOf()
        ownerID = toCopy.ownerID
        typeID = toCopy.typeID
        symbol = toCopy.symbol
        value = toCopy.value
        bodyColor = toCopy.bodyColor
        pos = VecInt2(toCopy.pos)
        pPos = VecInt2(toCopy.pPos)

        textColor.r = toCopy.textColor.r
        textColor.g = toCopy.textColor.g
        textColor.b = toCopy.textColor.b

        for (a in toCopy.abilities) {
            abilities += Ability(a)
        }
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
                a.action(game, movePos, this, a)
                break
            }
        }
    }

    fun move(newPos: VecInt2) {
        pPos.set(pos)
        pos.set(newPos)
        moveProgress = 0f
    }

    fun draw(viewport: ScalingViewport, game: BoardGame, dt: Float, moveTime: Float) {
        if (selected) {
            drawValidMoveTiles(game)
        }
        val px = pPos.x * Board.TILE_SIZE
        val py = pPos.y * Board.TILE_SIZE
        val cx = pos.x * Board.TILE_SIZE
        val cy = pos.y * Board.TILE_SIZE

        val x = Interpolation.pow2.apply(px, cx, moveProgress)
        val y = Interpolation.pow2.apply(py, cy, moveProgress)

        BoardApp.shapeDrawer.setColor(0f, 0f, 0f, shadowOpacity)
        CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding - shadowDist, y + padding - shadowDist, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        BoardApp.shapeDrawer.setColor(bodyColor)
        CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding, y + padding, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        TextRenderer.begin(BoardApp.batch, viewport, TextRenderer.Font.HEAVY, Board.TILE_SIZE, 0f)
        TextRenderer.color = textColor
        TextRenderer.drawTextCentered(x + Board.TILE_SIZE / 2f, y + Board.TILE_SIZE / 2f, symbol, Board.TILE_SIZE / 32, .33f)
        TextRenderer.end()

        if (moveTime == 0.0f) {
            moveProgress = 1f
        } else {
            moveProgress += dt / moveTime
            moveProgress = moveProgress.coerceAtMost(1f)
        }
    }

    fun drawValidMoveTiles(game: BoardGame) {
        BoardApp.shapeDrawer.setColor(1f, 1f, 0f, 1f)
        getAllValidActionPos(game).forEach {
            val x = it.x * Board.TILE_SIZE
            val y = it.y * Board.TILE_SIZE
            CustomShapes.filledRoundedRect(BoardApp.shapeDrawer, x + padding, y + padding, Board.TILE_SIZE - padding * 2, Board.TILE_SIZE - padding * 2, roundness)
        }
    }

    fun select() {
        selected = true
    }

    fun unselect() {
        selected = false
    }
}