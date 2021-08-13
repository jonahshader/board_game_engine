package jonahshader

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import jonahshader.screens.MenuScreen
import jonahshader.systems.assets.Assets
import jonahshader.systems.screen.ScreenManager
import space.earlygrey.shapedrawer.ShapeDrawer

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class BoardApp : Game() {
    companion object {
        lateinit var batch: SpriteBatch
            private set
        lateinit var shapeDrawer: ShapeDrawer
            private set
        lateinit var inputMultiplexer: InputMultiplexer
            private set
    }

    override fun create() {
        Assets.startLoading()
        Assets.finishLoading()
        batch = SpriteBatch()
        inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer
        shapeDrawer = ShapeDrawer(batch, Assets.getSprites().findRegion("white_pixel"))
        ScreenManager.game = this
        ScreenManager.push(MenuScreen())
    }

    override fun dispose() {
        batch.dispose()
        super.dispose()
    }
}