package pl.jojczak.birdhunt.stages.gameplay

sealed class GameplayUIAction {
    data object NavigateToSettings : GameplayUIAction()
}
