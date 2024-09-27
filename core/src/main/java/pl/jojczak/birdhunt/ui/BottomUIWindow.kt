package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

abstract class BottomUIWindow(
    title: String,
    skin: Skin,
    styleName: String,
) : Window(
    title,
    skin,
    styleName
) {
    private fun getShowAction(actor: Actor) = RunnableAction().apply {
        setRunnable {
            actor.isVisible = true
        }
    }

    private fun getHideAction(actor: Actor) = RunnableAction().apply {
        setRunnable {
            actor.isVisible = false
        }
    }

    private fun getDelayAction() = DelayAction(FLASH_DELAY)

    fun getFlashingAnim(actor: Actor, callback: () -> Unit = {}) = SequenceAction(
        SequenceAction(
            SequenceAction(
                getHideAction(actor),
                getDelayAction()
            ),
            SequenceAction(
                getShowAction(actor),
                getDelayAction()
            ),
            SequenceAction(
                getHideAction(actor),
                getDelayAction()
            ),
            SequenceAction(
                getShowAction(actor),
                getDelayAction()
            )
        ),
        SequenceAction(
            SequenceAction(
                getHideAction(actor),
                getDelayAction()
            ),
            SequenceAction(
                getShowAction(actor),
                getDelayAction()
            ),
            RunnableAction().apply {
                setRunnable {
                    callback()
                }
            }
        )
    )

    init {
        isMovable = false
        isResizable = false
    }

    companion object {
        const val FLASH_DELAY = 0.3f
    }
}
