package pl.jojczak.birdhunt.assetsloader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter
import com.badlogic.gdx.graphics.Texture
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
        Asset.I18N to Pair("i18n/texts", I18NBundle::class.java),
        Asset.UI_SKIN to Pair("myUi/jj_pixel_ui.json", Skin::class.java),
        Asset.TX_LOGO to Pair("logo.png", Texture::class.java),
        Asset.TX_BIRD to Pair("bird.png", Texture::class.java),
        Asset.TX_SCOPE to Pair("scope.png", Texture::class.java),
    )

    private class UnfinishedAssetLoadingException : RuntimeException()

    private const val TAG = "AssetsLoader"
}
