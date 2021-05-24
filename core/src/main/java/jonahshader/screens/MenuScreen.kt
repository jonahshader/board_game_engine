package jonahshader.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahshader.BoardApp
import jonahshader.systems.screen.ScreenManager
import jonahshader.systems.settings.Settings
import jonahshader.systems.ui.TextRenderer
import jonahshader.systems.ui.menu.Menu
import ktx.app.KtxScreen
import ktx.graphics.begin

class MenuScreen : KtxScreen {
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(640f, 900f, camera)
    private val menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(), Vector2(500f, 90f))

    init {
        menu.addMenuItem("Game Generator") {ScreenManager.push(GameGeneratorScreen())}
        menu.addMenuItem("Settings") { ScreenManager.push(SettingsScreen())}
        menu.addMenuItem("Exit") { Gdx.app.exit()}

        if((Settings.settings["fullscreen"] as String).toBoolean()) Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
    }

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.25f, .25f, .25f, 1f)

        viewport.apply()
        menu.run(delta, viewport)
        BoardApp.batch.begin(camera)
        menu.draw(BoardApp.batch, BoardApp.shapeDrawer, viewport)
        BoardApp.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}