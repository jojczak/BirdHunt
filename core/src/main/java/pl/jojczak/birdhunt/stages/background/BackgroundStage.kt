package pl.jojczak.birdhunt.stages.background

import pl.jojczak.birdhunt.actors.background.FarLandsActor
import pl.jojczak.birdhunt.base.BaseStage

class BackgroundStage : BaseStage() {
    private val farLands = FarLandsActor()

    init {
        root.color.a = 1f
        addActor(farLands)
    }

    companion object {
        private const val TAG = "BackgroundStage"
    }
}
