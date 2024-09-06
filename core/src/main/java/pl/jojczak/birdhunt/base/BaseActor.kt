package pl.jojczak.birdhunt.base

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class BaseActor : Actor() {
    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(batch.color.r, batch.color.g, batch.color.b, parentAlpha)
        super.draw(batch, parentAlpha)
    }
}
