package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Screen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.stages.background.BackgroundStage

abstract class BaseScreen<ActionType>(
    protected val mainActionReceiver: (action: MainAction) -> Unit,
    private val backgroundStage: BackgroundStage? = null
) : Screen {
    override fun show() = Unit

    override fun render(delta: Float) {
        backgroundStage?.act(delta)
        backgroundStage?.draw()
    }

    override fun resize(width: Int, height: Int) = Unit

    override fun pause() = Unit

    override fun resume() = Unit

    override fun hide() = Unit

    override fun dispose() = Unit

    open fun onAction(action: ActionType) = Unit
}
