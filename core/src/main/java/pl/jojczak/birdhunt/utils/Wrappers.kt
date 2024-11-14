package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class ButtonListener(
    private val onClick: (event: ChangeEvent, actor: Actor) -> Unit
) : ChangeListener() {
    override fun changed(event: ChangeEvent, actor: Actor) {
        onClick(event, actor)
    }
}

class DisabledButtonListener(
    private val onClick: (event: InputEvent) -> Unit
): ClickListener() {
    override fun clicked(event: InputEvent, x: Float, y: Float) {
        onClick(event)
    }
}
