package pl.jojczak.birdhunt.stages.background

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.stages.background.actors.MovableBackgroundActor
import pl.jojczak.birdhunt.stages.background.actors.BackgroundActor
import pl.jojczak.birdhunt.utils.InsetsHelper
import pl.jojczak.birdhunt.utils.insetsHelperInstance

class BackgroundStage : BaseStage() {
    private val fog1 = Image(AssetsLoader.get<Texture>(Asset.TX_BG_FOG))
    private val fog2 = Image(AssetsLoader.get<Texture>(Asset.TX_BG_FOG))

    private val mountains = MovableBackgroundActor(
        textureAsset = Asset.TX_BG_MOUNTAIN,
        startYPos = 50f,
        scale = 2.5f
    )

    private val farLands = MovableBackgroundActor(
        textureAsset = Asset.TX_BG_FAR_LANDS,
        startYPos = 0f,
        scale = 3f
    )

    private val clouds = BackgroundActor(
        textureAsset = Asset.TX_BG_CLOUDS
    )

    private val onInsetsChanged = InsetsHelper.OnInsetsChangedListener { _ ->
        onResize(Gdx.graphics.width, Gdx.graphics.height)
    }

    init {
        insetsHelperInstance.addOnInsetsChangedListener(onInsetsChanged)
        root.color.a = 1f
        addActor(mountains)
        addActor(fog1)
        addActor(fog2)
        addActor(farLands)
        addActor(clouds)
    }

    override fun act(delta: Float) {
        super.act(delta)

        fog1.x -= FOG_SPEED * delta
        fog2.x += FOG_SPEED * delta
        if (fog1.x + fog1.width * fog1.scaleX < viewport.worldWidth) fog1.x += (fog1.width * fog1.scaleX) / 2
        if (fog2.x > 0) fog2.x -= (fog2.width * fog2.scaleX) / 2
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)

        val insets = insetsHelperInstance.lastInsets
        insets.top.realToGameSize().let {
            if (it == 0f) clouds.isVisible = false
            else {
                clouds.isVisible = true
                clouds.y = viewport.worldHeight - it - CLOUDS_PADDING
            }
        }

        if (fog1.height < viewport.worldHeight) {
            fog1.setScale(viewport.worldHeight / fog1.height)
            fog2.setScale(viewport.worldHeight / fog1.height)
        } else if (fog1.width / 2 < viewport.worldWidth) {
            fog1.setScale(viewport.worldWidth / (fog1.width / 2))
            fog2.setScale(viewport.worldWidth / (fog1.width / 2))
        } else if (fog1.height >= viewport.worldHeight) {
            fog1.setScale(1f)
            fog2.setScale(1f)
        } else if (fog1.width / 2 >= viewport.worldWidth) {
            fog1.setScale(1f)
            fog2.setScale(1f)
        }
    }

    override fun dispose() {
        insetsHelperInstance.removeOnInsetsChangedListener(onInsetsChanged)
        super.dispose()
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "BackgroundStage"

        private const val FOG_SPEED = 20f
        private const val CLOUDS_PADDING = 3f
    }
}
