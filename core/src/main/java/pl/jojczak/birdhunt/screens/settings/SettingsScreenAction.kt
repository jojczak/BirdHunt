package pl.jojczak.birdhunt.screens.settings

sealed class SettingsScreenAction {
    data object NavigateToMainMenu : SettingsScreenAction()
}
