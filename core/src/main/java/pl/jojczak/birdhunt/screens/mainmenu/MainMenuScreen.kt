package pl.jojczak.birdhunt.screens.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.screens.mainmenu.stages.MainMenuStage

class MainMenuScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<MainMenuScreenAction>(
    mainActionReceiver = mainActionReceiver
) {
    private val backgroundStage = BackgroundStage()

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
        ScreenUtils.clear(Color.SKY)

        backgroundStage.act(delta)
        backgroundStage.draw()
        mainMenuStage.act(delta)
        mainMenuStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize MainMenuScreen width: $width, height: $height")
        super.resize(width, height)
        mainMenuStage.onResize(width, height)
        backgroundStage.onResize(width, height)
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose MainMenuScreen")
        mainMenuStage.dispose()
        backgroundStage.dispose()
        super.dispose()
    }

    override fun onAction(action: MainMenuScreenAction) {
        Gdx.app.log(TAG, "action received: $action")

        when (action) {
            MainMenuScreenAction.NavigateToGameplay -> {
                mainMenuStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToGameplay)
                }
            }

            MainMenuScreenAction.NavigateToSettings -> {
                mainMenuStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToSettings)
                }
            }

            MainMenuScreenAction.NavigateToAbout -> {
                mainMenuStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToAbout)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainMenuScreen"
    }
}
