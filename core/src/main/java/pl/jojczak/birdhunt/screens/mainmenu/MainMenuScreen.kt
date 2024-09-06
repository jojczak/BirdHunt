package pl.jojczak.birdhunt.screens.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction

class MainMenuScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<MainMenuScreenAction>(
    mainActionReceiver = mainActionReceiver
) {
    private val mainMenuStage = MainMenuStage(::onAction).apply {
        fadeIn()
    }

    override fun show() {
        Gdx.app.log(TAG, "show MainMenuScreen")
        super.show()
        Gdx.input.inputProcessor = mainMenuStage
    }

    override fun render(delta: Float) {
        super.render(delta)
        ScreenUtils.clear(Color.BLACK)

        mainMenuStage.act(delta)
        mainMenuStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize MainMenuScreen width: $width, height: $height")
        super.resize(width, height)
        mainMenuStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose MainMenuScreen")
        super.dispose()
        mainMenuStage.dispose()
    }

    override fun onAction(action: MainMenuScreenAction) {
        Gdx.app.log(TAG, "action received: $action")

        when (action) {
            MainMenuScreenAction.StartGame -> {
                mainMenuStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToGameplay)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainMenuScreen"
    }
}
