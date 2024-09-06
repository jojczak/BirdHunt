package pl.jojczak.birdhunt.assetsloader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

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
            assetManager?.load(asset.value.first, asset.value.second)
        }
    }

    fun get(asset: Asset) : Any {
        Gdx.app.log(TAG, "get $asset")
        if (!assetManager!!.update()) throw UnfinishedAssetLoadingException()

        return assetManager!!.get(assets[asset]!!.first, assets[asset]!!.second)
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

    private val assets = mapOf(
        Asset.TX_LOGO to Pair("logo.png", Texture::class.java)
    )

    private class UnfinishedAssetLoadingException: RuntimeException()

    private const val TAG = "AssetsLoader"
}
