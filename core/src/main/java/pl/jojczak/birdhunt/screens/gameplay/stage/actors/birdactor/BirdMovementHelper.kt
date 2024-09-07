package pl.jojczak.birdhunt.screens.gameplay.stage.actors.birdactor

import com.badlogic.gdx.Gdx
import kotlin.math.cos
import kotlin.math.sin

class BirdMovementHelper(
    private val baseSpeed: Float
) {
    private var movementType: BirdMovementType = BirdMovementType.RightBottom()

    fun update(
        bird: BirdActor,
        delta: Float
    ) {
        var newX = bird.x + (cos(movementType.angle) * (baseSpeed * delta))
        var newY = bird.y + (sin(movementType.angle) * (baseSpeed * delta))

        if (newX < 0) {
            newX = 0f
            movementType = when (movementType) {
                is BirdMovementType.LeftTop -> BirdMovementType.RightTop()
                else /* BirdMovementType.LeftBottom */ -> BirdMovementType.RightBottom()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        } else if (newX + bird.width > bird.stage.width) {
            newX = bird.stage.width - bird.width
            movementType = when (movementType) {
                is BirdMovementType.RightTop -> BirdMovementType.LeftTop()
                else /* BirdMovementType.RightBottom */ -> BirdMovementType.LeftBottom()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        }

        if (newY < 0) {
            newY = 0f
            movementType = when (movementType) {
                is BirdMovementType.LeftBottom -> BirdMovementType.LeftTop()
                else /* BirdMovementType.RightBottom */ -> BirdMovementType.RightTop()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        } else if (newY + bird.height > bird.stage.height) {
            newY = bird.stage.height - bird.height
            movementType = when (movementType) {
                is BirdMovementType.LeftTop -> BirdMovementType.LeftBottom()
                else /* BirdMovementType.RightTop */ -> BirdMovementType.RightBottom()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        }

        bird.setPosition(newX, newY)
    }

    companion object {
        private const val TAG = "BirdMovementManager"
    }
}
