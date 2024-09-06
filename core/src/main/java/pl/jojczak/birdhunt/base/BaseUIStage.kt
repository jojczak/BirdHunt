package pl.jojczak.birdhunt.base

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport

abstract class BaseUIStage: Stage(
    ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT)
) {
    companion object {
        private const val WORLD_WIDTH = 1080f
        private const val WORLD_HEIGHT = 1920f
    }
}
