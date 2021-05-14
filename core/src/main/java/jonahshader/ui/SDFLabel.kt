package jonahshader.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.singletons.TextRenderer

class SDFLabel(var text: String, private val font: TextRenderer.Font, private val viewport: ScalingViewport, var centered: Boolean) : Widget() {
    var boldness = 0f
    init {
        touchable = Touchable.disabled
    }
    override fun draw(batch: Batch, parentAlpha: Float) {
        TextRenderer.color.set(color.r, color.g, color.b, color.a * parentAlpha)
        TextRenderer.begin(batch, viewport, font, height, boldness)
        if (centered) {
            TextRenderer.drawTextCentered(x + width/2f, y + height/2f, text)
        } else {
            TextRenderer.drawText(x, y, text)
        }
        TextRenderer.end()
    }

    override fun getMinWidth(): Float {
        return width
    }

    override fun getMinHeight(): Float {
        return height
    }

    override fun getPrefWidth(): Float {
        return width
    }

    override fun getPrefHeight(): Float {
        return height
    }

    override fun getMaxWidth(): Float {
        return width
    }

    override fun getMaxHeight(): Float {
        return height
    }
}