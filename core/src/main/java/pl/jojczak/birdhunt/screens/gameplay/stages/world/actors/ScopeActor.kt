package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.utils.PREF_NAME
import pl.jojczak.birdhunt.utils.PREF_SENSITIVITY
import pl.jojczak.birdhunt.utils.PREF_SENSITIVITY_DEFAULT
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

class ScopeActor(
    private val gameplayHelper: GameplayHelper
): BaseActor(), SPenHelper.EventListener {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_SCOPE)

    private val preferences = Gdx.app.getPreferences(PREF_NAME)
    private var sensitivity = preferences.getFloat(PREF_SENSITIVITY, PREF_SENSITIVITY_DEFAULT)

    init {
        setSize(
            texture.width.toFloat(),
            texture.height.toFloat()
        )
        sPenHelperInstance.addEventListener(this)
        gameplayHelper.addGameplayListener(GameplayEventListener())
    }

    override fun onStage() {
        super.onStage()
        centerOnScreen()
    }

    private fun centerOnScreen() {
        setPosition(
            stage.width / 2,
            stage.height / 2
        )
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (hasParent()) {
            if (x < 0f) x = 0f
            if (x > stage.width) x = stage.width
            if (y < 0f) y = 0f
            if (y > stage.height) y = stage.height
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(texture, x - width / 2, y - height / 2)
    }

    override fun remove(): Boolean {
        sPenHelperInstance.removeEventListener(this)
        return super.remove()
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) = Unit

    override fun onSPenMotionEvent(x: Float, y: Float) {
        if (gameplayHelper.getState() is GameplayState.Paused) return

        this.x += x * sensitivity * MOTION_MULTIPLIER
        this.y += y * sensitivity * MOTION_MULTIPLIER
    }

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun resumed() {
            sensitivity = preferences.getFloat(PREF_SENSITIVITY, PREF_SENSITIVITY_DEFAULT)
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "ScopeActor"

        private const val MOTION_MULTIPLIER = 10f
    }
}
