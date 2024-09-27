package pl.jojczak.birdhunt.actors.birdactor

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.actors.birdactor.BirdActor.Companion.BASE_SPEED
import kotlin.math.cos
import kotlin.math.sin

class BirdMovementHelper(
    private val onMovementChanged: (BirdMovementType) -> Unit
) {
    private var movementType: BirdMovementType = listOf(
        BirdMovementType.RightBottom(),
        BirdMovementType.LeftTop(),
        BirdMovementType.RightTop(),
        BirdMovementType.LeftBottom()
    ).random().also { onMovementChanged(it) }
        set(value) {
            field = value
            onMovementChanged(value)
        }

    fun update(
        bird: BirdActor,
        delta: Float
    ) {
        var newX = bird.x + (cos(movementType.angle) * (BASE_SPEED * delta))
        var newY = bird.y + (sin(movementType.angle) * (BASE_SPEED * delta))

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
