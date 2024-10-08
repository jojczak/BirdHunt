package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.GameplayUIStage
import pl.jojczak.birdhunt.screens.gameplay.stages.world.GameplayStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.sPenHelperInstance

class GameplayScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?
) : BaseScreen<GameplayScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
) {
    private val gameplayHelper: ScreenGameplayHelper = ScreenGameplayHelper(::onAction)
    private val gameplayStage = GameplayStage(gameplayHelper)
    private val gameplayUIStage = GameplayUIStage(gameplayHelper)

    override fun show() {
        Gdx.app.log(TAG, "show gameplay screen")
        super.show()
        sPenHelperInstance.addEventListener(gameplayHelper)
        Gdx.input.inputProcessor = gameplayUIStage
        gameplayStage.fadeIn()
        gameplayUIStage.fadeIn()
    }

    override fun render(delta: Float) {
        super.render(delta)

        gameplayHelper.act(delta)
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
            is GameplayScreenAction.Exit -> {
                gameplayStage.fadeOut()
                gameplayUIStage.fadeOut {
                    mainActionReceiver(MainAction.NavigateToMainMenu)
                }
            }
        }
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose GameplayScreen")
        sPenHelperInstance.removeEventListener(gameplayHelper)
        gameplayStage.dispose()
        gameplayUIStage.dispose()
        super.dispose()
    }

    companion object {
        private const val TAG = "GameplayScreen"
    }
}
