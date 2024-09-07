package pl.jojczak.birdhunt.base

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class BaseActor : Actor() {
    private var isStage = false

    override fun act(delta: Float) {
        super.act(delta)
        if (hasParent() && !isStage) {
            onStage()
            isStage = true
        }
    }

    open fun onStage() = Unit

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(batch.color.r, batch.color.g, batch.color.b, parentAlpha)
        super.draw(batch, parentAlpha)
    }
}
