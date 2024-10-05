package pl.jojczak.birdhunt.stages.background.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor

class FarLandsActor : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_FAR_LANDS)

    private var orientationVertical = true

    private val actionOrientationHeight = FloatAction().also { aH ->
        aH.duration = ANIM_DUR
        aH.start = y
        aH.end = -texture.height.toFloat() / 4
        aH.interpolation = Interpolation.fastSlow
    }

    init {
        setSize(texture.width.toFloat(), texture.height.toFloat())
    }

    override fun act(delta: Float) {
        super.act(delta)
        y = actionOrientationHeight.value
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            texture,
            x,
            y,
            width / 2,
            height / 2,
            width,
            height,
            scaleX,
            scaleY,
            rotation,
            0,
            0,
            texture.width,
            texture.height,
            false,
            false
        )
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        val worldWidth = stage.viewport.worldWidth

        setScale(
            if (worldWidth > width) worldWidth / width
            else 1f
        )

        x = (worldWidth - width) / 2f

        if (scrWidth > scrHeight && orientationVertical) {
            orientationVertical = false
            actionOrientationHeight.isReverse = false
            actionOrientationHeight.restart()
            addAction(actionOrientationHeight)
        } else if (scrWidth < scrHeight && !orientationVertical) {
            orientationVertical = true
            actionOrientationHeight.isReverse = true
            actionOrientationHeight.restart()
            addAction(actionOrientationHeight)
        }
    }

    companion object {
        private const val TAG = "FarLandsActor"

        private const val ANIM_DUR = 0.5f
    }
}
