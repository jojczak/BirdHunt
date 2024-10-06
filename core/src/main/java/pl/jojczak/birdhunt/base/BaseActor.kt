package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class BaseActor : Actor() {
    protected var isStage = false

    override fun act(delta: Float) {
        super.act(delta)
        if (hasParent() && !isStage) {
            isStage = true
            onStage()
        }
    }

    open fun onStage() = Unit

    protected fun Number.toGameSize(): Float {
        if (stage == null) {
            Gdx.app.log(TAG, "Can't convert to game size, stage is null")
            return 0f
        }
        return stage.viewport.unproject(Vector2(this.toFloat(), 0f)).x
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(batch.color.r, batch.color.g, batch.color.b, parentAlpha)
        super.draw(batch, parentAlpha)
    }

    open fun onResize(scrWidth: Int, scrHeight: Int) = Unit

    companion object {
        private const val TAG = "BaseActor"
    }
}
