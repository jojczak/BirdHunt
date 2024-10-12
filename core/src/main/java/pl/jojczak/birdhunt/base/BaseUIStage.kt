package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.ExtendViewport
import pl.jojczak.birdhunt.utils.InsetsHelper
import pl.jojczak.birdhunt.utils.insetsHelperInstance

abstract class BaseUIStage : BaseStage(
    viewportMinWidth = WORLD_WIDTH,
    viewportMinHeight = WORLD_HEIGHT
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

        val ratioScale = getViewportScaleByRatio(scrWidth, scrHeight)

        viewport = ExtendViewport(
            viewportMinWidth * ratioScale * gameScale,
            viewportMinHeight * ratioScale * gameScale
        ).apply {
            update(horizontalMaxWidth, verticalMaxHeight, true)
            screenY = intents.bottom
            screenX = intents.left
        }
    }

    override fun dispose() {
        insetsHelperInstance.removeOnInsetsChangedListener(onInsetsChanged)
        super.dispose()
    }

    companion object {
        const val WORLD_WIDTH = 1080f
        const val WORLD_HEIGHT = 1080f
    }
}
