package pl.jojczak.birdhunt.stages

import com.badlogic.gdx.math.Vector2
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.actors.BulletActor
import pl.jojczak.birdhunt.actors.ScopeActor
import pl.jojczak.birdhunt.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

class GameplayStage : BaseStage(), SPenHelper.EventListener {
    private val birdActor = BirdActor()
    private val scopeActor = ScopeActor()
    private val shotgunActor = ShotgunActor()

    init {
        addActor(birdActor)
        addActor(scopeActor)
        addActor(shotgunActor)
        sPenHelperInstance.addEventListener(this)
        sPenHelperInstance.registerSPenEvents()
    }

    override fun act(delta: Float) {
        shotgunActor.scopePosition = Vector2(scopeActor.x, scopeActor.y)
        super.act(delta)
    }

    private fun gunShot() {
        addActor(
            BulletActor(
                startPos = Vector2(shotgunActor.getBarrelPos()),
                endPos = Vector2(scopeActor.x, scopeActor.y),
                angle = shotgunActor.angleToScope.degrees
            )
        )
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) {
        when (event) {
            SPenHelper.ButtonEvent.DOWN -> gunShot()
            SPenHelper.ButtonEvent.UP -> {}
            SPenHelper.ButtonEvent.UNKNOWN -> {}
        }
    }

    override fun onSPenMotionEvent(x: Float, y: Float) {

    }

    override fun dispose() {
        sPenHelperInstance.removeEventListener(this)
        sPenHelperInstance.unregisterSPenEvents()
        super.dispose()
    }

    companion object {
        private const val TAG = "GameplayStage"

        const val BIRDS_PER_ROUND = 6
    }
}
