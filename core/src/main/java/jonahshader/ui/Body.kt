package jonahshader.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils.PI
import com.badlogic.gdx.scenes.scene2d.Group
import space.earlygrey.shapedrawer.ShapeDrawer

open class Body(var roundness: Float, private val drawer: ShapeDrawer) : Group() {
    private val sides = 80
    override fun draw(batch: Batch, parentAlpha: Float) {
        drawer.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        drawer.filledRectangle(x, y + roundness, width, height - 2*roundness)
        drawer.filledRectangle(x + roundness, y, width - 2*roundness, roundness)
        drawer.filledRectangle(x + roundness, y + height - roundness, width - 2*roundness, roundness)
        drawer.sector(x + roundness, y + roundness, roundness, PI, PI/2, sides)
        drawer.sector(x + width - roundness, y + roundness, roundness, 3*PI/2, PI/2, sides)
        drawer.sector(x + width - roundness, y + height - roundness, roundness, 0f, PI/2, sides)
        drawer.sector(x + roundness, y + height - roundness, roundness, PI/2, PI/2, sides)
    }
}