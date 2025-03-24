package pl.jojczak.birdhunt.screens.gameplay.stages.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.base.BaseUIStage.Companion.WORLD_WIDTH
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.ScoreWidget

class GameplayUIStageInputListener(
    private val gameplayLogic: GameplayLogic
): InputListener() {
    private var lastDragX = 0f
    private var lastDragY = 0f

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (!arrayOf(GameplayUIStage.NAME, ScoreWidget.NAME).contains(event?.target?.name)) return false

        if (pointer == 0) {
            lastDragX = (x / WORLD_WIDTH) * BaseStage.WORLD_WIDTH
            lastDragY = (y / WORLD_WIDTH) * BaseStage.WORLD_WIDTH
        } else if (pointer == 1) {
            gameplayLogic.onAction(GameplayLogic.ToActions.DisablePGSByTouch)
            gameplayLogic.onAction(GameplayLogic.ToActions.Shot)
        }

        Gdx.app.log(TAG, "touchDown: $x, $y, $pointer")
        return true
    }

    override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
        if (pointer == 0) {
            val scaledX = (x / WORLD_WIDTH) * BaseStage.WORLD_WIDTH
            val scaledY = (y / WORLD_WIDTH) * BaseStage.WORLD_WIDTH

            val dx = scaledX - lastDragX
            val dy = scaledY - lastDragY

            if (dx > PGS_DISABLE_THRESHOLD || dy > PGS_DISABLE_THRESHOLD) {
                gameplayLogic.onAction(GameplayLogic.ToActions.DisablePGSByTouch)
            }

            lastDragX = scaledX
            lastDragY = scaledY

            gameplayLogic.onAction(
                GameplayLogic.ToActions.ScopeMoved(
                    dx / TOUCH_TO_S_PEN_SCALE,
                    dy / TOUCH_TO_S_PEN_SCALE
                )
            )

            Gdx.app.log(TAG, "touchDragged: $dx, $dy")
        }
        super.touchDragged(event, x, y, pointer)
    }

    companion object {
        private const val TAG = "GameplayUIStageInputListener"

        private const val TOUCH_TO_S_PEN_SCALE = 100f
        private const val PGS_DISABLE_THRESHOLD = 0.7f
    }
}
