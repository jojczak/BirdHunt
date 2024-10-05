package pl.jojczak.birdhunt.stages.background

import pl.jojczak.birdhunt.stages.background.actors.FarLandsActor
import pl.jojczak.birdhunt.base.BaseStage

class BackgroundStage : BaseStage() {
    private val farLands = FarLandsActor()

    init {
        root.color.a = 1f
        addActor(farLands)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "BackgroundStage"
    }
}
