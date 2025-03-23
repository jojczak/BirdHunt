package pl.jojczak.birdhunt.base

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Table

abstract class BaseTable : Table() {
    private val lastSize: Vector2 = Vector2()
    protected var isOrientationVertical = true

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

    override fun act(delta: Float) {
        super.act(delta)

        if (lastSize.x != width || lastSize.y != height) {
            if (height > width && !isOrientationVertical) {
                onOrientationChange(true)
                isOrientationVertical = true
            } else if (height < width && isOrientationVertical) {
                onOrientationChange(false)
                isOrientationVertical = false
            }
        }

        lastSize.x = width
        lastSize.y = height
    }

    open fun onOrientationChange(vertical: Boolean) = Unit

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

    fun hide() {
        isVisible = false
        color.a = 0f
    }

    fun show() {
        isVisible = true
        color.a = 1f
    }

    companion object {
        private const val FADE_DURATION = 0.20f
    }
}
