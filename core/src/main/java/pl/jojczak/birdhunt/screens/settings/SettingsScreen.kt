package pl.jojczak.birdhunt.screens.settings

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.settings.stages.SettingsStage

class SettingsScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<SettingsScreenAction>(
    mainActionReceiver
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
        ScreenUtils.clear(Color.SKY)

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
}
