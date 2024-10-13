package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import pl.jojczak.birdhunt.base.DisposableActor
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_SENSITIVITY
import pl.jojczak.birdhunt.utils.SPenHelper
import pl.jojczak.birdhunt.utils.insetsHelperInstance
import pl.jojczak.birdhunt.utils.realToStage
import pl.jojczak.birdhunt.utils.sPenHelperInstance

class ScopeActor(
    private val gameplayLogic: GameplayLogic
): BaseActor(), DisposableActor, SPenHelper.EventListener, Preferences.PreferenceListener<Float> {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_SCOPE)

    private var sensitivity = Preferences.get(PREF_SENSITIVITY)
    var bottomUISize = 0f

    init {
        Preferences.addListener(PREF_SENSITIVITY, this)
        sPenHelperInstance.addEventListener(this)

        setSize(
            texture.width.toFloat(),
            texture.height.toFloat()
        )
    }

    override fun onStage() {
        super.onStage()
        centerOnScreen()
    }

    fun centerOnScreen() {
        setPosition(
            stage.width / 2,
            stage.height / 2
        )
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (hasParent()) {
            val topInset = insetsHelperInstance.lastInsets.top.realToStage(stage)

            if (x < 0f) x = 0f
            if (x > stage.width) x = stage.width
            if (y < bottomUISize) y = bottomUISize
            if (y > stage.height - topInset) y = stage.height - topInset
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(texture, x - width / 2, y - height / 2)
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) = Unit

    override fun onSPenMotionEvent(x: Float, y: Float) {
        val gameplayState = gameplayLogic.onAction(GameplayLogic.ToActions.GetState)
        if (gameplayState.paused || gameplayState is GameplayState.GameOver) return

        this.x += x * sensitivity * MOTION_MULTIPLIER
        this.y += y * sensitivity * MOTION_MULTIPLIER
    }

    override fun onPreferenceChanged(value: Float) {
        sensitivity = value
    }

    override fun dispose() {
        Preferences.removeListener(PREF_SENSITIVITY, this)
        sPenHelperInstance.addEventListener(this)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "ScopeActor"

        private const val MOTION_MULTIPLIER = 10f
    }
}
