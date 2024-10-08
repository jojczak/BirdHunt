package pl.jojczak.birdhunt.screens.gameplay

sealed class GameplayScreenAction {
    data object NavigateToMainMenu : GameplayScreenAction()
}
