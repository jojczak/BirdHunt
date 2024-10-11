package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage

fun Number.realToStage(stage: Stage): Float = with(stage) {
    Vector3(this@realToStage.toFloat(), 0f, 0f).apply {
        x = (2 * x) / viewport.screenWidth - 1
        prj(camera.invProjectionView)
    }.x
}

fun Number.stageToReal(stage: Stage): Float = with(stage) {
    Vector3(this@stageToReal.toFloat(), 0f, 0f).apply {
        prj(camera.combined)
        x = viewport.screenWidth * (x + 1) / 2
    }.x
}
