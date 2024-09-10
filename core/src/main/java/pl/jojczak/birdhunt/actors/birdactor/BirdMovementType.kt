package pl.jojczak.birdhunt.actors.birdactor

import kotlin.math.PI
import kotlin.random.Random

sealed class BirdMovementType {
    protected abstract val baseAngle: Float
    abstract val angle: Float

    protected fun getRandomAngleForQuadrant(): Float {
        val halfQuadrantSize = (Math.PI.toFloat() / 4f) * 0.8f
        val max = baseAngle + halfQuadrantSize
        val min = baseAngle - halfQuadrantSize
        return Random.nextFloat() * (max - min) + min
    }

    override fun toString() = "radians: $angle, Degree: ${angle * (180f / Math.PI.toFloat())}°"

    class LeftTop : BirdMovementType() {
        override val baseAngle = 3 * PI.toFloat() / 4f
        override val angle = getRandomAngleForQuadrant()
        override fun toString() = "LeftTop ${super.toString()}"
    }

    class RightTop : BirdMovementType() {
        override val baseAngle = PI.toFloat() / 4f
        override val angle = getRandomAngleForQuadrant()
        override fun toString() = "RightTop ${super.toString()}"
    }

    class LeftBottom : BirdMovementType() {
        override val baseAngle = 5 * PI.toFloat() / 4f
        override val angle = getRandomAngleForQuadrant()
        override fun toString() = "LeftBottom ${super.toString()}"
    }

    class RightBottom : BirdMovementType() {
        override val baseAngle = 7 * PI.toFloat() / 4f
        override val angle = getRandomAngleForQuadrant()
        override fun toString() = "RightBottom ${super.toString()}"
    }
}
