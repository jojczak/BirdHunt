package pl.jojczak.birdhunt.screens.about

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.about.stages.AboutStage

class AboutScreen(
    mainActionReceiver: (action: MainAction) -> Unit
): BaseScreen<AboutScreenAction>(
    mainActionReceiver
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
        ScreenUtils.clear(Color.SKY)

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

    companion object {
        private const val TAG = "AboutScreen"
    }
}
