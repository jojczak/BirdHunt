package pl.jojczak.birdhunt.screens.about

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.about.stages.AboutStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage

class AboutScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?,
): BaseScreen<AboutScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
) {
    private val aboutStage = AboutStage(::onAction).also { aS ->
        aS.fadeIn()
    }

    override fun show() {
        super.show()
        Gdx.app.log(TAG, "Show")
        Gdx.app.input.inputProcessor = aboutStage
    }

    override fun render(delta: Float) {
        super.render(delta)

        aboutStage.act(delta)
        aboutStage.draw()
    }

    override fun onAction(action: AboutScreenAction) {
        when(action) {
            AboutScreenAction.NavigateToMainMenu -> {
                mainActionReceiver(MainAction.NavigateToMainMenu)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        aboutStage.onResize(width, height)
    }

    override fun dispose() {
        aboutStage.dispose()
        super.dispose()
    }

    companion object {
        private const val TAG = "AboutScreen"
    }
}
