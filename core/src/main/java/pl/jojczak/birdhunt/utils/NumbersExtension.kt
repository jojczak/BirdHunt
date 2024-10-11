package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage

fun Number.realToStage(stage: Stage): Float {
    return stage.screenToStageCoordinates(Vector2(this.toFloat(), 0f)).x
}

fun Number.stageToReal(stage: Stage): Float {
    return stage.stageToScreenCoordinates(Vector2(this.toFloat(), 0f)).x
}
