package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.os.helpers.osCoreHelper
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.GameplayUIStage
import pl.jojczak.birdhunt.screens.gameplay.stages.world.GameplayStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.SoundManager
import pl.jojczak.birdhunt.os.helpers.sPenHelperInstance

class GameplayScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?,
    soundManager: SoundManager
) : BaseScreen<GameplayScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
) {
    private val gameplayLogic = GameplayLogicImpl(soundManager, ::onAction)
    private val gameplayStage = GameplayStage(gameplayLogic)
    private val gameplayUIStage = GameplayUIStage(gameplayLogic) { gameplayStage.bottomUISize = it }

    override fun show() {
        Gdx.app.log(TAG, "show gameplay screen")
        super.show()
        osCoreHelper.setKeepScreenOn(true)
        Gdx.input.inputProcessor = gameplayUIStage
        gameplayLogic.addActionsListener(gameplayStage)
        gameplayStage.fadeIn()
        gameplayUIStage.fadeIn()
    }

    override fun render(delta: Float) {
        super.render(delta)

        gameplayLogic.act(delta)

        gameplayStage.act(delta)
        gameplayStage.draw()
        gameplayUIStage.act(delta)
        gameplayUIStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize GameplayScreen width: $width, height: $height")
        super.resize(width, height)
        gameplayStage.onResize(width, height)
        gameplayUIStage.onResize(width, height)
    }

    override fun onAction(action: GameplayScreenAction) {
        super.onAction(action)
        Gdx.app.log(TAG, "onAction: $action")

        when (action) {
            is GameplayScreenAction.NavigateToMainMenu -> {
                gameplayStage.fadeOut()
                gameplayUIStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToMainMenu)
                }
            }
        }
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose GameplayScreen")
        gameplayLogic.removeActionsListener(gameplayStage)
        sPenHelperInstance.removeEventListener(gameplayLogic)
        osCoreHelper.setKeepScreenOn(false)
        gameplayStage.dispose()
        gameplayUIStage.dispose()
        gameplayLogic.dispose()
        super.dispose()
    }

    companion object {
        private const val TAG = "GameplayScreen"
    }
}
