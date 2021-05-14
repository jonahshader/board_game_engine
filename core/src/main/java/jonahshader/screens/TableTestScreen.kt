package jonahshader.screens

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.singletons.TextRenderer
import jonahshader.ui.SDFLabel
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import kotlin.math.pow

class TableTestScreen : KtxScreen {
    private lateinit var stage: Stage
    private var time = 0f

    private lateinit var nameLabel: SDFLabel
    private lateinit var nameText: SDFLabel
    private lateinit var addressLabel: SDFLabel
    private lateinit var addressText: SDFLabel

    override fun show() {
        stage = Stage(FitViewport(640f, 480f))

        nameLabel = SDFLabel("Name:", TextRenderer.Font.NORMAL, stage.viewport as ScalingViewport, false)
        nameText = SDFLabel("(your name):", TextRenderer.Font.NORMAL, stage.viewport as ScalingViewport, false)
        addressLabel = SDFLabel("Address:", TextRenderer.Font.NORMAL, stage.viewport as ScalingViewport, false)
        addressText = SDFLabel("(your address):", TextRenderer.Font.NORMAL, stage.viewport as ScalingViewport, false)

        val table = Table()
        table.add(nameLabel).width(100f)
        table.add(nameText).width(100f)
        table.row()
        table.add(addressLabel).width(100f)
        table.add(addressText).width(100f)

        stage.addActor(table)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        stage.act(delta)
        stage.draw()

        time += delta
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
    }
}