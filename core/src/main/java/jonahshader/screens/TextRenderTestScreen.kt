package jonahshader.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahshader.singletons.Assets
import jonahshader.singletons.TextRenderer
import ktx.app.KtxScreen
import ktx.graphics.begin

/** First screen of the application. Displayed after the application is created.  */
class TextRenderTestScreen(private val batch: SpriteBatch) : KtxScreen {
    private val screenWidth = 640f
    private val screenHeight = 360f
    private val viewport = FitViewport(screenWidth, screenHeight)

    private var scale = 1f
    private var time = 0f

    override fun show() {
        // Prepare your screen here.
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        scale = viewport.scaling.apply(screenWidth, screenHeight, Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        ).x / screenWidth
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        batch.begin(viewport.camera!!)
        TextRenderer.begin(batch, viewport, TextRenderer.Font.LIGHT, 64 * (cos(time * .5f) * .9f + 1.1f), 0f)
        TextRenderer.drawTextCentered(screenWidth/2f, 100 + screenHeight/2f, "Hello! Testing")
        TextRenderer.end()
        TextRenderer.begin(batch, viewport, TextRenderer.Font.NORMAL, 64 * (cos(time * .5f + 2*PI/3) * .9f + 1.1f), 0f)
        TextRenderer.drawTextCentered(screenWidth/2f, 0 + screenHeight/2f, "Hello! Testing")
        TextRenderer.end()
        TextRenderer.begin(batch, viewport, TextRenderer.Font.HEAVY, 64 * (cos(time * .5f + 4*PI/3) * .9f + 1.1f), 0f)
        TextRenderer.drawTextCentered(screenWidth/2f, -100 + screenHeight/2f, "Hello! Testing")
        TextRenderer.end()
        batch.end()
//        batch.begin(viewport.camera!!)
//        batch.shader = Assets.dffShader
//
//        val normalScale = cos(time * .5f + 4*PI/3) + 1.1f
//        Assets.dffShader.setUniformf("p_distOffset", 0f)
//        Assets.dffShader.setUniformf("p_spread", 4f)
//        Assets.dffShader.setUniformf("p_renderScale", scale * normalScale)
//        Assets.normalFont.setUseIntegerPositions(false)
//        Assets.normalFont.data.setScale(normalScale)
//        Assets.normalFont.draw(batch, "Hello!???", screenWidth/2f, 100 + screenHeight/2f + Assets.normalFont.capHeight/2f, 0f, Align.center, false)
//
//        batch.shader = null
//        batch.shader = Assets.dffShader
//
//        val heavyScale = cos(time * .5f) + 1.1f
//        Assets.dffShader.setUniformf("p_distOffset", 0f)
//        Assets.dffShader.setUniformf("p_spread", 6f)
//        Assets.dffShader.setUniformf("p_renderScale", scale * heavyScale)
//        Assets.heavyFont.setUseIntegerPositions(false)
//        Assets.heavyFont.data.setScale(heavyScale)
//        Assets.heavyFont.draw(batch, "Hello!???", screenWidth/2f, screenHeight/2f + Assets.heavyFont.capHeight/2f, 0f, Align.center, false)
//
//        batch.shader = null
//        batch.shader = Assets.dffShader
//
//        val lightScale = cos(time * .5f + 2*PI/3) + 1.1f
//        Assets.dffShader.setUniformf("p_distOffset", 0f)
//        Assets.dffShader.setUniformf("p_spread", 3f)
//        Assets.dffShader.setUniformf("p_renderScale", scale * lightScale)
//        Assets.lightFont.setUseIntegerPositions(false)
//        Assets.lightFont.data.setScale(lightScale)
//        Assets.lightFont.draw(batch, "Hello!???", screenWidth/2f, -100 + screenHeight/2f + Assets.lightFont.capHeight/2f, 0f, Align.center, false)
//        batch.end()

        time += delta
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        scale = viewport.scaling.apply(viewport.worldWidth, viewport.worldHeight, Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        ).x / viewport.worldWidth
        println(scale)
    }

    override fun dispose() {
    }
}