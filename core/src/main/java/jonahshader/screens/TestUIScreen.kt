package jonahshader.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahshader.singletons.Assets
import jonahshader.singletons.TextRenderer
import jonahshader.ui.Body
import jonahshader.ui.SDFLabel
import ktx.actors.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Table
import jonahshader.ui.Button
import ktx.app.KtxScreen
import space.earlygrey.shapedrawer.ShapeDrawer
import java.lang.Math.pow
import kotlin.math.pow

class TestUIScreen : KtxScreen {
    private lateinit var stage: Stage
    private var time = 0f

    private lateinit var label: SDFLabel

    override fun show() {
        stage = Stage(FitViewport(640f, 480f))
        Gdx.input.inputProcessor = stage

        val drawer = ShapeDrawer(stage.batch, Assets.getSprites().findRegion("white_pixel"))

        val mainTable = Table()
//        mainTable.fill

        val mainGroup = Group()
        stage.addActor(mainGroup)

        val mainBody = Body(0f, drawer)
        mainBody.setSize(640f, 480f)
        mainBody.color.set(0f, 0f, 0f, 0f)
        mainGroup.addActor(mainBody)

        val body = Body(15f, drawer)
        body.setSize(320f, 240f)
        mainBody.addActor(body)
        body.setPosition(60f, 60f)
//        body.centerPosition()
        mainGroup.addActor(body)

        label = SDFLabel("Hello", TextRenderer.Font.HEAVY, stage.viewport as ScalingViewport, true)
        label.color.set(0f, 0f, 0f, 1f)
        label.setSize(160f, 60f)
        body.addActor(label)
        label.centerPosition()
        val action = moveBy(0f, 30f, 0.75f)
        action.interpolation = Interpolation.pow2Out
        val returnAction = moveBy(0f, -30f, 0.75f)
        returnAction.interpolation = Interpolation.bounceOut
        val sequence = repeat(500, action then returnAction)
        label += sequence
        mainGroup.addActor(label)

        val button = Button(8f, 6f, drawer, "Button", TextRenderer.Font.LIGHT, stage.viewport as ScalingViewport, stage)
        button.setSize(120f, 40f)
        button.setColor(1f, 1f, 1f, 1f)
        button.label.setColor(0f, 0f, 0f, 1f)
        button.setPosition(20f, 20f)

        button += moveTo(320f, 240f, 5f)

        mainGroup.addActor(button)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        stage.act(delta)
        stage.draw()

//        label.centered = (time.rem(2f) < 1)
        label.boldness = (-cos(time * PI * 2 / 1.5f)*.5f + .5f).pow(3) * .05f

        time += delta
    }

    override fun dispose() {
        stage.dispose()
    }
}