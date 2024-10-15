package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor.Companion.BASE_SPEED
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor.Companion.SPEED_PER_ROUND
import pl.jojczak.birdhunt.utils.InsetsHelper
import pl.jojczak.birdhunt.utils.insetsHelperInstance
import pl.jojczak.birdhunt.utils.realToStage
import kotlin.math.cos
import kotlin.math.sin

class BirdMovementHelper(
    private val onMovementChanged: (BirdMovementType) -> Unit,
    private val gameplayLogic: GameplayLogic
): InsetsHelper.OnInsetsChangedListener {
    var bottomUISize = 0f
    private var stage: Stage? = null
    private var insets: InsetsHelper.WindowInsetsF = InsetsHelper.WindowInsetsF()

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
        if (stage == null && bird.hasParent()) {
            stage = bird.stage
            onInsetsChanged(insetsHelperInstance.lastInsets)
        }

        val round = minOf(gameplayLogic.onAction(GameplayLogic.ToActions.GetRound), 10)
        val speed = BASE_SPEED + round * SPEED_PER_ROUND

        var newX = bird.x + (cos(movementType.angle) * (speed * delta))
        var newY = bird.y + (sin(movementType.angle) * (speed * delta))

        if (newX < insets.left) {
            newX = insets.left
            movementType = when (movementType) {
                is BirdMovementType.LeftTop -> BirdMovementType.RightTop()
                else /* BirdMovementType.LeftBottom */ -> BirdMovementType.RightBottom()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        } else if (newX + bird.width > bird.stage.width - insets.right) {
            newX = bird.stage.width - bird.width - insets.right
            movementType = when (movementType) {
                is BirdMovementType.RightTop -> BirdMovementType.LeftTop()
                else /* BirdMovementType.RightBottom */ -> BirdMovementType.LeftBottom()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        }

        if (newY < bottomUISize + insets.bottom && bird.aboveBorder) {
            newY = bottomUISize + insets.bottom
            movementType = when (movementType) {
                is BirdMovementType.LeftBottom -> BirdMovementType.LeftTop()
                else /* BirdMovementType.RightBottom */ -> BirdMovementType.RightTop()
            }
            Gdx.app.log(TAG, "New movementType: $movementType")
        } else if (newY + bird.height > bird.stage.height - insets.top) {
            newY = bird.stage.height - bird.height - insets.top
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

    override fun onInsetsChanged(insets: InsetsHelper.WindowInsets) {
        stage?.let { stage ->
            this.insets = InsetsHelper.WindowInsetsF(
                top = insets.top.realToStage(stage),
                bottom = insets.bottom.realToStage(stage),
                left = insets.left.realToStage(stage),
                right = insets.right.realToStage(stage)
            )
        }
    }
}
