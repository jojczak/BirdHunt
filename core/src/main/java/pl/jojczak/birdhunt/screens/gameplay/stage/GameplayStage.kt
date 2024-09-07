package pl.jojczak.birdhunt.screens.gameplay.stage

import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.BirdActor
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.ScopeActor
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

class GameplayStage : BaseStage(), SPenHelper.EventListener {
    private val birdActor = BirdActor()
    private val scopeActor = ScopeActor()

    init {
        addActor(birdActor)
        addActor(scopeActor)
        sPenHelperInstance.registerSPenEvents()
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
