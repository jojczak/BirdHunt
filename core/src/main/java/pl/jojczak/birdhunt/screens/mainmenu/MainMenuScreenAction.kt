package pl.jojczak.birdhunt.screens.mainmenu

sealed class MainMenuScreenAction {
    data object FirstFrameDrawn : MainMenuScreenAction()
    data object NavigateToGameplay : MainMenuScreenAction()
    data object NavigateToSettings : MainMenuScreenAction()
    data object NavigateToAbout: MainMenuScreenAction()
}
