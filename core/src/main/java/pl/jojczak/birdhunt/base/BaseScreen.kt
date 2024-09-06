package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Screen
import pl.jojczak.birdhunt.main.MainAction

abstract class BaseScreen(
    protected val mainActionReceiver: (action: MainAction) -> Unit
) : Screen {
    override fun show() = Unit

    override fun render(delta: Float) = Unit

    override fun resize(width: Int, height: Int) = Unit

    override fun pause() = Unit

    override fun resume() = Unit

    override fun hide() = Unit

    override fun dispose() = Unit
}
