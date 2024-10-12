package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_GAME_SCALE

abstract class BaseStage(
    protected val viewportMinWidth: Float = WORLD_WIDTH,
    protected val viewportMinHeight: Float = WORLD_HEIGHT
) : Stage(
    ExtendViewport(viewportMinWidth, viewportMinHeight)
) {
    protected val skin = AssetsLoader.get<Skin>(Asset.UI_SKIN)
    protected val i18N = AssetsLoader.get<I18NBundle>(Asset.I18N)

    private var firstDraw = true
    protected var gameScale = Preferences.get(PREF_GAME_SCALE)

    private val gameScaleListener = Preferences.PreferenceListener<Float> {
        gameScale = it
        onResize(Gdx.graphics.width, Gdx.graphics.height)
    }

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
        Preferences.addListener(PREF_GAME_SCALE, gameScaleListener)
    }

    override fun draw() {
        viewport.apply()
        super.draw()

        if (firstDraw) {
            firstDraw = false
            onFirstFrame()
        }
    }

    fun fadeIn(callback: () -> Unit = {}) {
        addAction(fadeInAction(callback))
    }

    fun fadeOut(callback: () -> Unit = {}) {
        addAction(fadeOutAction(callback))
    }

    open fun onFirstFrame() = Unit

    open fun onResize(scrWidth: Int, scrHeight: Int) {
        val ratioScale = getViewportScaleByRatio(scrWidth, scrHeight)

        viewport = ExtendViewport(
            viewportMinWidth * ratioScale * gameScale,
            viewportMinHeight * ratioScale * gameScale
        )
        viewport.update(scrWidth, scrHeight, true)

        for (actor in actors) {
            if (actor is BaseActor) {
                actor.onResize(scrWidth, scrHeight)
            }
        }
    }

    protected fun getViewportScaleByRatio(scrWidth: Int, scrHeight: Int) = maxOf(
        1f,
        if (scrWidth < scrHeight) {
            (scrWidth.toFloat() / scrHeight) * 1.6f
        } else {
            (scrHeight.toFloat() / scrWidth) * 1.6f
        }
    )

    override fun dispose() {
        Preferences.removeListener(PREF_GAME_SCALE, gameScaleListener)
        for (actor in actors) {
            if (actor is DisposableActor) actor.onDispose()
        }
        super.dispose()
    }

    override fun actorRemoved(actor: Actor) {
        if (actor is DisposableActor) actor.onDispose()
        super.actorRemoved(actor)
    }

    companion object {
        const val WORLD_WIDTH = 200f
        const val WORLD_HEIGHT = 200f

        private const val FADE_DURATION = 0.20f
    }
}
