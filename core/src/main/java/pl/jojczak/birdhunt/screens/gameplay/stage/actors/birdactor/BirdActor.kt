package pl.jojczak.birdhunt.screens.gameplay.stage.actors.birdactor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor

class BirdActor : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_BIRD)
    private val textureFrames = TextureRegion.split(texture, FRAME_SIZE, FRAME_SIZE)
    private val deadTexture = textureFrames[0][textureFrames[0].size - 1]

    private val animationHelper = BirdAnimationHelper(
        animationFrames = Array(textureFrames[0].size - 1) { i -> textureFrames[0][i] },
        frameDuration = FRAME_DURATION
    )

    private val movementHelper = BirdMovementHelper(
        baseSpeed = BASE_SPEED
    )

    init {
        setSize(FRAME_SIZE.toFloat(), FRAME_SIZE.toFloat())
    }

    override fun onStage() {
        super.onStage()
        setPosition(
            stage.width / 2 - width / 2,
            stage.height / 2 - height / 2
        )
    }

    override fun act(delta: Float) {
        super.act(delta)
        animationHelper.update(delta)
        movementHelper.update(this, delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(animationHelper.getFrame(), x, y)
    }

    companion object {
        private const val FRAME_SIZE = 32
        private const val FRAME_DURATION = 0.075f
        private const val BASE_SPEED = 100f
    }
}
