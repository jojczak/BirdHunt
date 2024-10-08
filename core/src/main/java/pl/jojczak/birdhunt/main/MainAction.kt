package pl.jojczak.birdhunt.main

sealed class MainAction {
    data object SetupBackgroundStage: MainAction()
    data object NavigateToMainMenu : MainAction()
    data object NavigateToGameplay : MainAction()
    data object NavigateToSettings : MainAction()
    data object NavigateToAbout : MainAction()
}
