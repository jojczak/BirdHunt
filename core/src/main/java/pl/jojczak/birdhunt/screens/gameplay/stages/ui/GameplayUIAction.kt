package pl.jojczak.birdhunt.screens.gameplay.stages.ui

sealed class GameplayUIAction {
    data object NavigateToSettings : GameplayUIAction()
    data object NavigateToControls : GameplayUIAction()
}
