package pl.jojczak.birdhunt.screens.settings

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.settings.stages.SettingsStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage

class SettingsScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?
) : BaseScreen<SettingsScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
) {
    private val settingsStage = SettingsStage(true, ::onAction).apply {
        fadeIn()
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = settingsStage
    }

    override fun render(delta: Float) {
        super.render(delta)

        settingsStage.act(delta)
        settingsStage.draw()
    }

    override fun onAction(action: SettingsScreenAction) {
        when (action) {
            SettingsScreenAction.NavigateToMainMenu -> {
                mainActionReceiver(MainAction.NavigateToMainMenu)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        settingsStage.onResize(width, height)
    }

    override fun dispose() {
        settingsStage.dispose()
        super.dispose()
    }
}
