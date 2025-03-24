package pl.jojczak.birdhunt.screens.gameplay

import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor

sealed class GameplayScreenAction {
    data object NavigateToMainMenu : GameplayScreenAction()
    data class ShowRetroEffect(val birds: List<BirdActor>): GameplayScreenAction()
}
