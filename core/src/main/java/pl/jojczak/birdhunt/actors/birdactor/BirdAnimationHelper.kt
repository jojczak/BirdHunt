package pl.jojczak.birdhunt.actors.birdactor

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import pl.jojczak.birdhunt.actors.birdactor.BirdActor.Companion.FRAME_DURATION
import pl.jojczak.birdhunt.actors.birdactor.BirdActor.Companion.MV_ANIM_DUR

class BirdAnimationHelper(
    animationFrames: Array<TextureRegion>
) {
    private val animation = Animation(FRAME_DURATION, *animationFrames)

    private var elapsedTime = 0f
    private var animationForward = true

    fun getFrame(): TextureRegion = animation.getKeyFrame(elapsedTime)

    fun update(delta: Float) {
        if (animationForward) {
            elapsedTime += delta
        } else {
            elapsedTime -= delta
        }

        if (elapsedTime >= animation.animationDuration) {
            elapsedTime = animation.animationDuration - FRAME_DURATION
            animationForward = false
        } else if (elapsedTime <= 0) {
            elapsedTime = FRAME_DURATION
            animationForward = true
        }
    }

    fun getAnimationForMovement(movement: BirdMovementType): Action = when (movement) {
        is BirdMovementType.LeftBottom -> ParallelAction(
            ScaleToAction().apply {
                x = -1f
                y = 1f
                duration = MV_ANIM_DUR
            },
            RotateToAction().apply {
                rotation = 80f
                duration = MV_ANIM_DUR
            }
        )

        is BirdMovementType.LeftTop -> ParallelAction(
            ScaleToAction().apply {
                x = -1f
                y = 1f
                duration = MV_ANIM_DUR
            },
            RotateToAction().apply {
                rotation = 0f
                duration = MV_ANIM_DUR
            }
        )

        is BirdMovementType.RightBottom -> ParallelAction(
            ScaleToAction().apply {
                x = 1f
                y = 1f
                duration = MV_ANIM_DUR
            },
            RotateToAction().apply {
                rotation = -80f
                duration = MV_ANIM_DUR
            }
        )

        is BirdMovementType.RightTop -> ParallelAction(
            ScaleToAction().apply {
                x = 1f
                y = 1f
                duration = MV_ANIM_DUR
            },
            RotateToAction().apply {
                rotation = 0f
                duration = MV_ANIM_DUR
            }
        )
    }
}
