package jonahshader.singletons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeType
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable

object Assets : Disposable {
    // Fonts
//    private const val LIGHT_FONT = "graphics/fonts/Mont-ExtraLightDEMO.ttf"
//    private const val LIGHT_FONT_LOAD_SIZE = 36
//    lateinit var lightFont: BitmapFont
//
//    private const val BOLD_FONT = "graphics/fonts/Mont-HeavyDEMO.ttf"
//    private const val BOLD_FONT_LOAD_SIZE = 36
//    lateinit var boldFont: BitmapFont

    // Fonts
    private const val LIGHT_FONT = "graphics/fonts/light_font"
    const val LIGHT_FONT_SPREAD = 3f
    const val LIGHT_FONT_SIZE = 64f
    private lateinit var lightFontTexture: Texture
    lateinit var lightFont: BitmapFont
    private const val NORMAL_FONT = "graphics/fonts/normal_font"
    const val NORMAL_FONT_SPREAD = 4f
    const val NORMAL_FONT_SIZE = 64f
    private lateinit var normalFontTexture: Texture
    lateinit var normalFont: BitmapFont
    private const val HEAVY_FONT = "graphics/fonts/heavy_font"
    const val HEAVY_FONT_SPREAD = 6f
    const val HEAVY_FONT_SIZE = 64f
    private lateinit var heavyFontTexture: Texture
    lateinit var heavyFont: BitmapFont

    // Sprites
    private const val SPRITES = "graphics/spritesheets/sprites.atlas"

    // Shaders
    private const val DFF_SHADER = "graphics/shaders/dff";
    lateinit var dffShader: ShaderProgram
        private set

    val manager = AssetManager()

    fun getSprites() : TextureAtlas {
        return manager.get(SPRITES)
    }

    fun startLoading() {
        loadFonts()
        loadTextures()
        loadShaders()
    }

    fun getProgress() : Float = manager.progress

    fun finishLoading() {
        manager.finishLoading()
    }

    private fun loadShaders() {
        dffShader = ShaderProgram(Gdx.files.internal("$DFF_SHADER.vert"), Gdx.files.internal("$DFF_SHADER.frag"))
        if (!dffShader.isCompiled) {
            Gdx.app.error("dffShader", "compilation failed:\n" + dffShader.log)
        }
    }

    private fun loadFonts() {
        lightFontTexture = Texture(Gdx.files.internal("$LIGHT_FONT.png"), true)
        heavyFontTexture = Texture(Gdx.files.internal("$HEAVY_FONT.png"), true)
        normalFontTexture = Texture(Gdx.files.internal("$NORMAL_FONT.png"), true)

        lightFontTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear)
        heavyFontTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear)
        normalFontTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear)

        lightFont = BitmapFont(Gdx.files.internal("$LIGHT_FONT.fnt"), TextureRegion(lightFontTexture), false)
        heavyFont = BitmapFont(Gdx.files.internal("$HEAVY_FONT.fnt"), TextureRegion(heavyFontTexture), false)
        normalFont = BitmapFont(Gdx.files.internal("$NORMAL_FONT.fnt"), TextureRegion(normalFontTexture), false)
    }

//    private fun loadFonts() {
////        val resolver = InternalFileHandleResolver()
////        manager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
////        manager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
//        var gen = FreeTypeFontGenerator(Gdx.files.internal(LIGHT_FONT))
//        var param = FreeTypeFontGenerator.FreeTypeFontParameter()
//        param.size = LIGHT_FONT_LOAD_SIZE
//        lightFont = gen.generateFont(param)
//        lightFont.region.texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear)
//        gen.dispose()
//
//        gen = FreeTypeFontGenerator(Gdx.files.internal(BOLD_FONT))
//        param = FreeTypeFontGenerator.FreeTypeFontParameter()
//        param.size = BOLD_FONT_LOAD_SIZE
//        boldFont = gen.generateFont(param)
//        boldFont.region.texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear)
//        gen.dispose()
//    }

    private fun loadTextures() {
        manager.load(SPRITES, TextureAtlas::class.java)
    }

    override fun dispose() {
        manager.dispose()
        lightFontTexture.dispose()
        lightFont.dispose()
        heavyFontTexture.dispose()
        heavyFont.dispose()
    }
}