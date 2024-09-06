package pl.jojczak.birdhunt.screens.gameplay.stage.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor

class BirdActor : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_BIRD)
    private val textureFrames = TextureRegion.split(texture, FRAME_SIZE, FRAME_SIZE)

    private val animationFrames: Array<TextureRegion> = Array(textureFrames[0].size - 1) { i -> textureFrames[0][i] }
    private val deadTexture = textureFrames[0][textureFrames[0].size - 1]

    private val textureAnimation = Animation(FRAME_DURATION, *animationFrames)
    private var animationElapsedTime = 0f
    private var animationForward = true

    override fun act(delta: Float) {
        super.act(delta)
        manageAnimation(delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(textureAnimation.getKeyFrame(animationElapsedTime), x, y)
    }

    private fun manageAnimation(delta: Float) {
        if (animationForward) {
            animationElapsedTime += delta
        } else {
            animationElapsedTime -= delta
        }

        if (animationElapsedTime >= textureAnimation.animationDuration) {
            animationElapsedTime = textureAnimation.animationDuration - FRAME_DURATION
            animationForward = false
        } else if (animationElapsedTime <= 0) {
            animationElapsedTime = FRAME_DURATION
            animationForward = true
        }
    }

    companion object {
        private const val FRAME_SIZE = 32
        private const val FRAME_DURATION = 0.075f
    }
}
