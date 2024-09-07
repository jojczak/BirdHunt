package pl.jojczak.birdhunt.base

import com.badlogic.gdx.utils.viewport.ExtendViewport

abstract class BaseUIStage : BaseStage(
    viewport = ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT)
) {
    companion object {
        private const val WORLD_WIDTH = 1080f
        private const val WORLD_HEIGHT = 1920f
    }
}
