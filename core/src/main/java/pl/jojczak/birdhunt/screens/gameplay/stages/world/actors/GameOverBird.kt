package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import pl.jojczak.birdhunt.base.DisposableActor

class GameOverBird(
    private val sign: Int
) : BaseActor(), DisposableActor {
    val texture = TextureRegion(AssetsLoader.get<Texture>(Asset.TX_BIRD), 2 * 32, 0, 32, 32)

    override fun onStage() {
        setSize(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
        choseNewAnimation()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(texture, x, y, width / 2, height / 2, width, height, scaleX, scaleY, rotation)
    }

    private fun choseNewAnimation() {
        val speed = (90..110).random() / 100f
        val yPos = (50..stage.height.toInt() - 50).random().toFloat()
        scaleX = sign.toFloat()

        if (sign == 1) {
            setPosition(-32f, yPos)
        } else {
            setPosition(stage.width, yPos)
        }

        rotation = 0f

        val animation = when ((1..ANIM_AMOUNT).random()) {
            1 -> getViewingAnimation(speed)
            2 -> getLaughingAnimation(speed)
            else -> get360Animation(speed)
        }

        addAction(animation)
    }

    override fun dispose() {

    }

    private fun getViewingAnimation(speed: Float) = SequenceAction(
        getPeekOutAnimation(speed),
        DelayAction(DELAY_DUR * speed),
        RotateByAction().apply {
            this.amount = -sign * SEEK_ROTATION
            this.duration = FAST_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        getPeekInAnimation(speed)
    )

    private fun get360Animation(speed: Float) = SequenceAction(
        getPeekOutAnimation(speed),
        DelayAction(DELAY_DUR * speed),
        ParallelAction(
            RotateByAction().apply {
                this.amount = -sign * FLIP_ROTATION
                this.duration = FLIP_DUR * speed
                this.interpolation = Interpolation.swing
            },
            SequenceAction(
                DelayAction(FAST_DUR * speed),
                MoveByAction().apply {
                    this.amountY = FLIP_Y_MOVE
                    this.duration = (FLIP_DUR / 2) * speed
                    this.interpolation = Interpolation.fastSlow
                },
                MoveByAction().apply {
                    this.amountY = -FLIP_Y_MOVE
                    this.duration = (FLIP_DUR / 2) * speed
                    this.interpolation = Interpolation.slowFast
                }
            )
        ),
        getPeekInAnimation(speed)
    )

    private fun getLaughingAnimation(speed: Float) = SequenceAction(
        getPeekOutAnimation(speed),
        DelayAction(DELAY_DUR * speed),
        SequenceAction(
            getLaughAnimation(speed),
            getLaughAnimation(speed),
            getLaughAnimation(speed),
            getLaughAnimation(speed),
            getPeekInAnimation(speed)
        )
    )

    private fun getLaughAnimation(speed: Float) = SequenceAction(
        getLaughStartAnim(speed),
        getLaughEndAnim(speed)
    )

    private fun getLaughStartAnim(speed: Float) = ParallelAction(
        RotateByAction().apply {
            this.amount = -sign * WAGGING_ROTATION
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        MoveByAction().apply {
            this.amountX = sign * WAGGING_X_MOVE
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        MoveByAction().apply {
            this.amountY = -WAGGING_Y_MOVE
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        }
    )

    private fun getLaughEndAnim(speed: Float) = ParallelAction(
        RotateByAction().apply {
            this.amount = sign * WAGGING_ROTATION
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        MoveByAction().apply {
            this.amountX = -sign * WAGGING_X_MOVE
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        MoveByAction().apply {
            this.amountY = WAGGING_Y_MOVE
            this.duration = WAGGING_DUR * speed
            this.interpolation = Interpolation.pow2
        }
    )

    private fun getPeekOutAnimation(speed: Float) = ParallelAction(
        RotateByAction().apply {
            this.amount = -sign * SEEK_ROTATION
            this.duration = SEEK_DUR * speed
            this.interpolation = Interpolation.pow2
        },
        MoveByAction().apply {
            this.amountX = sign * SEEK_X_MOVE
            this.duration = SEEK_DUR * speed
            this.interpolation = Interpolation.pow2
        }
    )

    private fun getPeekInAnimation(speed: Float) = SequenceAction(
        DelayAction((DELAY_DUR / 2f) * speed),
        ParallelAction(
            RotateByAction().apply {
                this.amount = sign * SEEK_ROTATION * 2
                this.duration = FAST_DUR * speed
                this.interpolation = Interpolation.pow2
            },
            MoveByAction().apply {
                this.amountX = -sign * SEEK_X_MOVE
                this.duration = SEEK_DUR * speed
                this.interpolation = Interpolation.pow2
            }
        ),
        DelayAction(DELAY_DUR * speed),
        RunnableAction().apply {
            setRunnable {
                choseNewAnimation()
            }
        }
    )

    companion object {
        private const val DELAY_DUR = 1f

        private const val SEEK_ROTATION = 20f
        private const val SEEK_X_MOVE = 18f
        private const val SEEK_DUR = 1f
        private const val FAST_DUR = 0.5f

        private const val WAGGING_ROTATION = 40f
        private const val WAGGING_X_MOVE = 3f
        private const val WAGGING_Y_MOVE = 3f
        private const val WAGGING_DUR = 0.5f

        private const val FLIP_ROTATION = 360f
        private const val FLIP_Y_MOVE = 32f
        private const val FLIP_DUR = 1.5f

        private const val ANIM_AMOUNT = 3
    }
}
