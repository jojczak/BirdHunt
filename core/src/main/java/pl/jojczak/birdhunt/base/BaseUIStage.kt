package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.ExtendViewport
import pl.jojczak.birdhunt.utils.InsetsHelper
import pl.jojczak.birdhunt.utils.insetsHelperInstance

abstract class BaseUIStage : BaseStage(
    viewport = ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT)
) {
    private val onInsetsChanged = InsetsHelper.OnInsetsChangedListener { _ ->
        onResize(Gdx.graphics.width, Gdx.graphics.height)
    }

    init {
        insetsHelperInstance.addOnInsetsChangedListener(onInsetsChanged)
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        val intents = insetsHelperInstance.lastInsets

        val horizontalMaxWidth = scrWidth - intents.left - intents.right
        val verticalMaxHeight = scrHeight - intents.top - intents.bottom

        viewport = ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT).apply {
            update(horizontalMaxWidth, verticalMaxHeight, true)
            screenY = intents.bottom
            screenX = intents.left
        }
    }

    protected fun Number.uiToGameSize(): Float {
        return this.toFloat() * WORLD_WIDTH / BaseStage.WORLD_WIDTH
    }

    override fun dispose() {
        insetsHelperInstance.removeOnInsetsChangedListener(onInsetsChanged)
        super.dispose()
    }

    companion object {
        const val WORLD_WIDTH = 1080f
        val WORLD_HEIGHT = if (Gdx.app.type == Application.ApplicationType.Android) {
            1080f
        } else {
            1728f //1920f
        }
    }
}
