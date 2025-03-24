package pl.jojczak.birdhunt.assetsloader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import java.util.Locale

object AssetsLoader {
    private var assetManager: AssetManager? = null

    fun startLoading() {
        Gdx.app.log(TAG, "Loading started")
        if (assetManager == null) {
            Gdx.app.log(TAG, "assetManager was null, creating new")
            assetManager = AssetManager()
        }

        for (asset in assets) {
            Gdx.app.log(TAG, "loading ${asset.key}")

            when (asset.value.second) {
                I18NBundle::class.java -> {
                    val bundleParameter = I18NBundleParameter(Locale.ENGLISH)
                    assetManager?.load(asset.value.first, I18NBundle::class.java, bundleParameter)
                }
                else -> {
                    assetManager?.load(asset.value.first, asset.value.second)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(asset: Asset): T {
        Gdx.app.log(TAG, "get $asset")
        if (!assetManager!!.update()) throw UnfinishedAssetLoadingException()

        return assetManager!!.get(assets[asset]!!.first, assets[asset]!!.second) as T
    }

    fun progress(): Float = assetManager!!.progress

    fun finished(): Boolean {
        return assetManager!!.update()
    }

    fun reset() {
        Gdx.app.log(TAG, "Resetting asset manager")
        assetManager?.dispose()
        assetManager = AssetManager()
    }

    fun dispose() {
        Gdx.app.log(TAG, "Disposing asset manager")
        assetManager?.dispose()
        assetManager = null
    }

    private val assets = mapOf(
        Asset.I18N                  to      Pair("i18n/texts",                  I18NBundle::class.java),
        Asset.UI_SKIN               to      Pair("jjPixelUI/JJPixelUI.json",    Skin::class.java),
        Asset.UI_BULLET             to      Pair("bullet_ui.png",               Texture::class.java),
        Asset.UI_S_PEN              to      Pair("spen_conn_icon.png",          Texture::class.java),
        Asset.TX_LOGO               to      Pair("logo.png",                    Texture::class.java),
        Asset.TX_BIRD               to      Pair("bird.png",                    Texture::class.java),
        Asset.TX_SCOPE              to      Pair("scope.png",                   Texture::class.java),
        Asset.TX_SHOTGUN            to      Pair("shotgun.png",                 Texture::class.java),
        Asset.TX_BULLET             to      Pair("bullet.png",                  Texture::class.java),
        Asset.TX_BG_FAR_LANDS       to      Pair("farlands.png",                Texture::class.java),
        Asset.TX_BG_FAR_LANDS_2     to      Pair("farlands2.png",               Texture::class.java),
        Asset.TX_BG_MOUNTAIN        to      Pair("mountain.png",                Texture::class.java),
        Asset.TX_BG_FOG             to      Pair("fog.png",                     Texture::class.java),
        Asset.TX_BG_CLOUDS          to      Pair("clouds.png",                  Texture::class.java),
        Asset.TX_CONTROL_S_PEN      to      Pair("controls_spen.png",           Texture::class.java),
        Asset.TX_CONTROL_TOUCH      to      Pair("controls_touch.png",          Texture::class.java),
        Asset.SN_START_COUNTDOWN    to      Pair("sounds/start_countdown.mp3",  Sound::class.java),
        Asset.SN_GUN_SHOT           to      Pair("sounds/gun_shot.mp3",         Sound::class.java),
        Asset.SN_GUN_RELOAD         to      Pair("sounds/gun_reload.mp3",       Sound::class.java),
        Asset.SN_BIRD_FALLING       to      Pair("sounds/bird_falling.mp3",     Sound::class.java),
        Asset.SN_BIRD_FLYING        to      Pair("sounds/bird_flying.mp3",      Sound::class.java),
        Asset.SN_LVL_UP             to      Pair("sounds/lvl_up.mp3",           Sound::class.java),
        Asset.SN_GAME_OVER          to      Pair("sounds/game_over.mp3",        Sound::class.java),
        Asset.PT_FEATHERS           to      Pair("particles/feathers.p",        ParticleEffect::class.java),
    )

    private class UnfinishedAssetLoadingException : RuntimeException()

    private const val TAG = "AssetsLoader"
}
