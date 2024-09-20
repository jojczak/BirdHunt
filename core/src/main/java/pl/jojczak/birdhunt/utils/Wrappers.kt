package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener

class ButtonListener(
    private val onClick: (event: ChangeEvent, actor: Actor) -> Unit
) : ChangeListener() {
    override fun changed(event: ChangeEvent, actor: Actor) {
        onClick(event, actor)
    }
}
