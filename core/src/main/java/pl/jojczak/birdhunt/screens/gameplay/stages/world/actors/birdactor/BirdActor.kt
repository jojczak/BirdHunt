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
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class BirdActor(
    private val gameplayHelper: GameplayHelper
) : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_BIRD)
    private val textureFrames = TextureRegion.split(texture, FRAME_SIZE, FRAME_SIZE)
    private val deadTexture = textureFrames[0][textureFrames[0].size - 1]

    private val gameplayEventListener = GameplayEventListener()

    var isDead = false
        private set

    private fun getDeadAnimation(onAnimEnd: () -> Unit) = SequenceAction(
        MoveToAction().apply {
            this.x = this@BirdActor.x
            this.y = this@BirdActor.y + 20f
            this.duration = DEAD_ANIM_DUR / 2
            this.interpolation = Interpolation.fastSlow
        },
        MoveToAction().apply {
            this.x = this@BirdActor.x
            this.y = -this@BirdActor.height
            this.duration = DEAD_ANIM_DUR
            this.interpolation = Interpolation.slowFast
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
            this.y = this@BirdActor.stage.height
            this.duration = DEAD_ANIM_DUR
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
        onMovementChanged = ::onMovementChanged
    )

    init {
        setSize(FRAME_SIZE.toFloat(), FRAME_SIZE.toFloat())
        gameplayHelper.addGameplayListener(gameplayEventListener)
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
        val gameplayState = gameplayHelper.getState()

        if (gameplayState is GameplayState.Paused || isDead) return
        animationHelper.update(delta)

        if (gameplayState is GameplayState.GameOver) return
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

    private fun onMovementChanged(movement: BirdMovementType) {
        addAction(animationHelper.getAnimationForMovement(movement))
    }

    override fun remove(): Boolean {
        Gdx.app.log(TAG, "Removing bird from stage")
        gameplayHelper.removeGameplayListener(gameplayEventListener)
        return super.remove()
    }

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun birdHit() {
            Gdx.app.log(TAG, "Starting dead animation")
            isDead = true
            addAction(getDeadAnimation {
                remove()
            })
        }

        override fun onGameplayStateChanged(state: GameplayState) {
            if (state is GameplayState.GameOver) {
                Gdx.app.log(TAG, "Starting game over animation")
                movementHelper.movementType = BirdMovementType.RightTop()
                addAction(getGameOverAnimation {
                    remove()
                })
            }
        }
    }

    companion object {
        private const val TAG = "BirdActor"

        private const val FRAME_SIZE = 32
        const val FRAME_DURATION = 0.075f
        const val BASE_SPEED = 100f
        const val MV_ANIM_DUR = 0.25f
        const val DEAD_ANIM_DUR = 0.7f
    }
}
