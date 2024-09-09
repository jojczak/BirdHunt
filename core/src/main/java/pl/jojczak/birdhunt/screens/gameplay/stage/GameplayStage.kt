package pl.jojczak.birdhunt.screens.gameplay.stage

import com.badlogic.gdx.math.Vector2
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.ScopeActor
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.ShotgunActor
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
        sPenHelperInstance.registerSPenEvents()
    }

    override fun act(delta: Float) {
        shotgunActor.scopePosition = Vector2(scopeActor.x, scopeActor.y)
        super.act(delta)
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) {

    }

    override fun onSPenMotionEvent(x: Float, y: Float) {

    }

    override fun dispose() {
        sPenHelperInstance.unregisterSPenEvents()
        super.dispose()
    }
}
