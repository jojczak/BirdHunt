package pl.jojczak.birdhunt.main

import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.SoundManager

sealed class MainAction {
    data class LoadingFinished(
        val bgStage: BackgroundStage,
        val soundManager: SoundManager
    ) : MainAction()

    data object FirstFrameDrawn : MainAction()
    data object NavigateToMainMenu : MainAction()
    data object NavigateToGameplay : MainAction()
    data object NavigateToSettings : MainAction()
    data object NavigateToAbout : MainAction()
}
