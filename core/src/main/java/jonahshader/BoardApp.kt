package jonahshader

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import jonahshader.screens.TableTestScreen
import jonahshader.screens.TestUIScreen
import jonahshader.screens.TextRenderTestScreen
import jonahshader.singletons.Assets
import jonahshader.singletons.ScreenManager

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class BoardApp : Game() {
    lateinit var batch: SpriteBatch

    override fun create() {
//        batch = SpriteBatch()
        ScreenManager.game = this
        Assets.startLoading()
        Assets.finishLoading()
//        ScreenManager.push(TextRenderTestScreen(batch))
        ScreenManager.push(TableTestScreen())
    }

    override fun dispose() {
//        batch.dispose()
        super.dispose()
    }
}