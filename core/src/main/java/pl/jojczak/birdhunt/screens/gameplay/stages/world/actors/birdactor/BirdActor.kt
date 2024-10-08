package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.screens.gameplay.stages.world.GameplayStage
import kotlin.random.Random

class BirdActor(
    private val gameplayLogic: GameplayLogic
) : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_BIRD)
    private val textureFrames = TextureRegion.split(texture, FRAME_SIZE, FRAME_SIZE)
    private val deadTexture = textureFrames[0][textureFrames[0].size - 1]

    var isDead = false
        private set

    var aboveBorder = false
        private set

    private fun getDeadAnimation(onAnimEnd: () -> Unit) = SequenceAction(
        MoveToAction().apply {
            this.x = this@BirdActor.x
            this.y = this@BirdActor.y + 20f
            this.duration = DEAD_ANIM_DUR / 3
            this.interpolation = Interpolation.pow2Out
        },
        MoveToAction().apply {
            this.x = this@BirdActor.x
            this.y = -200f
            this.duration = DEAD_ANIM_DUR
            this.interpolation = Interpolation.pow2In
        },
        RunnableAction().apply {
            setRunnable {
                onAnimEnd()
            }
        }
    )

    private fun getGameOverAnimation(onAnimEnd: () -> Unit) = SequenceAction(
        MoveToAction().apply {
            this.x = this@BirdActor.x
            this.y = this@BirdActor.stage.height * 2
            this.duration = DEAD_ANIM_DUR * 2
            this.interpolation = Interpolation.linear
        },
        RunnableAction().apply {
            setRunnable {
                onAnimEnd()
            }
        }
    )

    private val animationHelper = BirdAnimationHelper(
        animationFrames = Array(textureFrames[0].size - 1) { i -> textureFrames[0][i] }
    )

    private val movementHelper = BirdMovementHelper(
        onMovementChanged = ::onMovementChanged,
        gameplayLogic = gameplayLogic
    )

    init {
        setSize(FRAME_SIZE.toFloat(), FRAME_SIZE.toFloat())
    }

    override fun onStage() {
        super.onStage()
        setPosition(
            Random.nextFloat() * (stage.width - width),
            -height
        )
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (isDead) return

        if (!aboveBorder && y > GameplayStage.getBottomUIBorderSize()) aboveBorder = true

        animationHelper.update(delta)

        if (gameplayLogic.onAction(GameplayLogic.ToActions.GetState) is GameplayState.GameOver) return
        movementHelper.update(this, delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (isDead) {
            batch.draw(deadTexture, x, y, width / 2, height / 2, width, height, scaleX, scaleY, rotation)
        } else {
            batch.draw(animationHelper.getFrame(), x, y, width / 2, height / 2, width, height, scaleX, scaleY, rotation)
        }
    }

    fun onHit() = if (!isDead) {
        Gdx.app.log(TAG, "Bird hit")
        isDead = true
        addAction(getDeadAnimation { remove() })
        true
    } else {
        Gdx.app.log(TAG, "Bird is dead, can't be hit")
        false
    }

    fun onGameOver() {
        Gdx.app.log(TAG, "Bird game over")
        movementHelper.movementType = BirdMovementType.RightTop()
        addAction(getGameOverAnimation { remove() })
    }

    private fun onMovementChanged(movement: BirdMovementType) {
        addAction(animationHelper.getAnimationForMovement(movement))
    }

    override fun remove(): Boolean {
        Gdx.app.log(TAG, "Removing bird from stage")
        return super.remove()
    }

    companion object {
        private const val TAG = "BirdActor"

        private const val FRAME_SIZE = 32
        const val FRAME_DURATION = 0.075f
        const val BASE_SPEED = 90f
        const val SPEED_PER_ROUND = 10f
        const val MV_ANIM_DUR = 0.25f
        const val DEAD_ANIM_DUR = 0.75f
    }
}
