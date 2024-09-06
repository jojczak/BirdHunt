package pl.jojczak.birdhunt.main

sealed class MainAction {
    data object NavigateToMainMenu : MainAction()
}
