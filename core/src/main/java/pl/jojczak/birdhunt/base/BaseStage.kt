package pl.jojczak.birdhunt.base

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

abstract class BaseStage(
    viewport: Viewport? = null
) : Stage(
    viewport ?: ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT)
) {
    protected val skin = AssetsLoader.get<Skin>(Asset.UI_SKIN)
    protected val i18N = AssetsLoader.get<I18NBundle>(Asset.I18N)

    private fun fadeInAction(callback: () -> Unit) = SequenceAction(
        ColorAction().apply {
            this.color = root.color
            this.endColor = root.color.cpy().apply { a = 1f }
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
            this.color = root.color
            this.endColor = root.color.cpy().apply { a = 0f }
            this.duration = FADE_DURATION
        },
        RunnableAction().apply {
            setRunnable {
                callback()
            }
        }
    )

    init {
        root.color.a = 0f
    }

    fun fadeIn(callback: () -> Unit = {}) {
        addAction(fadeInAction(callback))
    }

    fun fadeOut(callback: () -> Unit = {}) {
        addAction(fadeOutAction(callback))
    }

    open fun onResize(scrWidth: Int, scrHeight: Int) {
        for (actor in actors) {
            if (actor is BaseActor) {
                actor.onResize(scrWidth, scrHeight)
            }
        }
    }

    override fun dispose() {
        for (actor in actors) {
            if (actor is SPenHelper.EventListener) {
                sPenHelperInstance.removeEventListener(actor)
            }
        }
        super.dispose()
    }

    override fun actorRemoved(actor: Actor) {
        if (actor is SPenHelper.EventListener) {
            sPenHelperInstance.removeEventListener(actor)
        }
        super.actorRemoved(actor)
    }

    companion object {
        const val WORLD_WIDTH = 200f
        const val WORLD_HEIGHT = 320f

        private const val FADE_DURATION = 0.20f
    }
}
