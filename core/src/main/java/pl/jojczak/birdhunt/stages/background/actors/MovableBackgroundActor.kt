package pl.jojczak.birdhunt.stages.background.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction
import pl.jojczak.birdhunt.assetsloader.Asset

class MovableBackgroundActor(
    textureAsset: Asset,
    startYPos: Float,
    scale: Float
) : BackgroundActor(
    textureAsset
) {
    private val actionOrientationHeight = FloatAction().also { aH ->
        aH.duration = ANIM_DUR
        aH.value = startYPos
        aH.start = startYPos
        aH.end = startYPos - texture.height.toFloat() / scale
        aH.interpolation = Interpolation.fastSlow
    }

    private var orientationVertical = true

    override fun act(delta: Float) {
        super.act(delta)
        y = actionOrientationHeight.value
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)

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
        private const val ANIM_DUR = 0.5f
    }
}
