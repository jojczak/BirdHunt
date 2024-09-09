package pl.jojczak.birdhunt.screens.gameplay.stage.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import kotlin.math.atan2

class ShotgunActor: BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_SHOTGUN)
    private val textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT)[0]

    var scopePosition = Vector2(0f, 0f)
        set(value) {
            if (value.y <= 1) value.y = 1f
            field = value
        }
    private var angleToScope = 0f

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
        angleToScope = angleRadians * (180f / Math.PI.toFloat()) - 90
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        when (angleToScope) {
            in -MAX_ANGLE..-MED_ANGLE -> batch.draw(textureFrames[0], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
            in -MED_ANGLE..-LOW_ANGLE -> batch.draw(textureFrames[1], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
            in -LOW_ANGLE..LOW_ANGLE -> batch.draw(textureFrames[2], x - X_OFFSET, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
            in LOW_ANGLE..MED_ANGLE -> batch.draw(textureFrames[1], x + X_OFFSET, y, 0f, 0f, width, height, -scaleX, scaleY, rotation)
            in MED_ANGLE..MAX_ANGLE -> batch.draw(textureFrames[0], x + X_OFFSET, y, 0f, 0f, width, height, -scaleX, scaleY, rotation)
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

    companion object {
        private const val TAG = "ShotgunActor"

        private const val FRAME_WIDTH = 26
        private const val FRAME_HEIGHT = 52

        private const val X_OFFSET = 5f

        private const val MAX_ANGLE = 90f
        private const val MED_ANGLE = 30f
        private const val LOW_ANGLE = 10f
    }
}
