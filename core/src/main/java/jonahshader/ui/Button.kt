package jonahshader.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.singletons.TextRenderer
import ktx.actors.*
import space.earlygrey.shapedrawer.ShapeDrawer

class Button(private val padding: Float, roundness: Float, drawer: ShapeDrawer, text: String, font: TextRenderer.Font, viewport: ScalingViewport, stage: Stage) : Body(roundness, drawer) {
    var label = SDFLabel(text, font, viewport, true)
    init {
        addActor(label)
        stage.addActor(label)

        addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                val action = Actions.moveBy(0f, padding, 0.25f)
                action.interpolation = Interpolation.circleOut
                label.addAction(action)
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                val action = Actions.moveBy(0f, -padding, 0.25f)
                action.interpolation = Interpolation.circleOut
                label.addAction(action)
            }
        })
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        label.setSize(width - padding * 2, height - padding * 2)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        label.setPosition(padding, padding)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
    }


}