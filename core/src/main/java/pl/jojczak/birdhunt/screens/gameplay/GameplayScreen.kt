package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.stages.gameplay.GameplayStage
import pl.jojczak.birdhunt.stages.gameplay.GameplayUIStage
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

class GameplayScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<GameplayScreenAction>(
    mainActionReceiver = mainActionReceiver
) {
    private val gameplayHelper: ScreenGameplayHelper = ScreenGameplayHelperImpl(::onAction)
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
        ScreenUtils.clear(Color.SKY)

        gameplayHelper.act(delta)
        gameplayStage.act(delta)
        gameplayUIStage.act(delta)
        gameplayStage.draw()
        gameplayUIStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize GameplayScreen width: $width, height: $height")
        super.resize(width, height)
        gameplayStage.viewport.update(width, height, true)
        gameplayUIStage.viewport.update(width, height, true)
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
