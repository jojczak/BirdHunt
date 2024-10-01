package pl.jojczak.birdhunt.base

import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Table

abstract class BaseTable : Table() {
    private fun fadeInAction(callback: () -> Unit) = SequenceAction(
        ColorAction().apply {
            this@apply.color = this@BaseTable.color
            this.endColor = this@BaseTable.color.cpy().apply { a = 1f }
            this.duration = FADE_DURATION
        },
        RunnableAction().apply {
            setRunnable {
                callback()
            }
        }
    )

    private fun fadeOutAction(callback: () -> Unit) = SequenceAction(
        ColorAction().apply {
            this@apply.color = this@BaseTable.color
            this.endColor = this@BaseTable.color.cpy().apply { a = 0f }
            this.duration = FADE_DURATION
        },
        RunnableAction().apply {
            setRunnable {
                callback()
            }
        }
    )

    init {
        color.a = 0f
        isVisible = false
    }

    fun fadeIn(callback: () -> Unit = {}) {
        isVisible = true
        addAction(fadeInAction(callback))
    }

    fun fadeOut(callback: () -> Unit = {}) {
        addAction(
            fadeOutAction {
                isVisible = false
                callback()
            }
        )
    }

    companion object {
        private const val FADE_DURATION = 0.20f
    }
}
