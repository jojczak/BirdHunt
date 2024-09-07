package pl.jojczak.birdhunt.screens.gameplay.stage.actors.birdactor

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class BirdAnimationHelper(
    animationFrames: Array<TextureRegion>,
    private val frameDuration: Float
) {
    private val animation = Animation(frameDuration, *animationFrames)

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
            elapsedTime = animation.animationDuration - frameDuration
            animationForward = false
        } else if (elapsedTime <= 0) {
            elapsedTime = frameDuration
            animationForward = true
        }
    }
}
