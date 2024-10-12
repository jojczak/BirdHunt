package pl.jojczak.birdhunt.screens.mainmenu

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.mainmenu.stages.MainMenuStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage

class MainMenuScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?
) : BaseScreen<MainMenuScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
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

        mainMenuStage.act(delta)
        mainMenuStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        mainMenuStage.onResize(width, height)
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose MainMenuScreen")
        mainMenuStage.dispose()
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

            MainMenuScreenAction.FirstFrameDrawn -> {
                mainActionReceiver(MainAction.FirstFrameDrawn)
            }
        }
    }

    companion object {
        private const val TAG = "MainMenuScreen"
    }
}
