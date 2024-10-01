package pl.jojczak.birdhunt.stages.settings

sealed class SettingsStageAction {
    data object NavigateToMainMenu : SettingsStageAction()
}
