package pl.jojczak.birdhunt.screens.gameplay.stage

import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.stage.actors.BirdActor

class GameplayStage : BaseStage() {
    private val birdActor = BirdActor()

    init {
        addActor(birdActor)
    }
}
