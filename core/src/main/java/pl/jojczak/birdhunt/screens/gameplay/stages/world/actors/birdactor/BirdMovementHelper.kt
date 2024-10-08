package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.stages.world.GameplayStage
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor.Companion.BASE_SPEED
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor.Companion.SPEED_PER_ROUND
import kotlin.math.cos
import kotlin.math.sin

class BirdMovementHelper(
    private val onMovementChanged: (BirdMovementType) -> Unit,
    private val gameplayLogic: GameplayLogic
) {
    var movementType: BirdMovementType = listOf(
        BirdMovementType.LeftTop(),
        BirdMovementType.RightTop()
    ).random().also { onMovementChanged(it) }
        set(value) {
            field = value
            onMovementChanged(value)
        }

    fun update(
        bird: BirdActor,
        delta: Float
    ) {
        val round = minOf(gameplayLogic.onAction(GameplayLogic.ToActions.GetRound), 10)
        val speed = BASE_SPEED + round * SPEED_PER_ROUND

        var newX = bird.x + (cos(movementType.angle) * (speed * delta))
        var newY = bird.y + (sin(movementType.angle) * (speed * delta))

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

        if (newY < GameplayStage.getBottomUIBorderSize() && bird.aboveBorder) {
            newY = GameplayStage.getBottomUIBorderSize()
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
