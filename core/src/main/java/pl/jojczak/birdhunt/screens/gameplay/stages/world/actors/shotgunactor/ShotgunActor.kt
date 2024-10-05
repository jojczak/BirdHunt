package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.shotgunactor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import kotlin.math.atan2

class ShotgunActor : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_SHOTGUN)
    private val textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT)[0]

    var scopePosition = Vector2(0f, 0f)
        set(value) {
            if (value.y <= 1) value.y = 1f
            field = value
        }
    var angleToScope: ShotgunAngle = ShotgunAngle.Center(0f)
        private set

    init {
        setSize(
            FRAME_WIDTH.toFloat(),
            FRAME_HEIGHT.toFloat()
        )
    }

    override fun onStage() {
        super.onStage()
        centerOnScreen()
    }

    override fun act(delta: Float) {
        super.act(delta)

        val deltaX = scopePosition.x - x
        val deltaY = scopePosition.y - y
        val angleRadians = atan2(deltaY, deltaX)
        angleToScope = ShotgunAngle.getAngle(angleRadians * (180f / Math.PI.toFloat()) - 90)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        when (angleToScope) {
            is ShotgunAngle.Left -> batch.draw(textureFrames[0], x + X_OFFSET, y, 0f, 0f, width, height, -scaleX, scaleY, rotation)
            is ShotgunAngle.SlLeft -> batch.draw(textureFrames[1], x + X_OFFSET, y, 0f, 0f, width, height, -scaleX, scaleY, rotation)
            is ShotgunAngle.Center -> batch.draw(textureFrames[2], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
            is ShotgunAngle.SlRight -> batch.draw(textureFrames[1], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
            is ShotgunAngle.Right -> batch.draw(textureFrames[0], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        centerOnScreen()
    }

    private fun centerOnScreen() {
        if (isStage) {
            setPosition(
                stage.width / 2,
                0f
            )
        }
    }

    fun getBarrelPos() = when (angleToScope) {
        is ShotgunAngle.Left -> Vector2(x - 24f, FRAME_HEIGHT.toFloat() - 2f)
        is ShotgunAngle.SlLeft -> Vector2(x - 9f, FRAME_HEIGHT.toFloat() + 2f)
        is ShotgunAngle.Center -> Vector2(x, FRAME_HEIGHT.toFloat() + 4f)
        is ShotgunAngle.SlRight -> Vector2(x + 9f, FRAME_HEIGHT.toFloat() + 2f)
        is ShotgunAngle.Right -> Vector2(x + 24f, FRAME_HEIGHT.toFloat() - 2f)
    }

    companion object {
        private const val TAG = "ShotgunActor"

        private const val FRAME_WIDTH = 26
        const val FRAME_HEIGHT = 52

        private const val X_OFFSET = 5f
    }
}
