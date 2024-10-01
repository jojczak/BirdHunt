package pl.jojczak.birdhunt.screens.mainmenu

sealed class MainMenuScreenAction {
    data object NavigateToGameplay : MainMenuScreenAction()
    data object NavigateToSettings : MainMenuScreenAction()
}
