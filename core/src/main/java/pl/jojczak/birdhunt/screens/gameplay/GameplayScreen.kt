package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.os.helpers.osCoreHelper
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.GameplayUIStage
import pl.jojczak.birdhunt.screens.gameplay.stages.world.GameplayStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.SoundManager
import pl.jojczak.birdhunt.os.helpers.sPenHelperInstance
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor

class GameplayScreen(
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?,
    soundManager: SoundManager
) : BaseScreen<GameplayScreenAction>(
    mainActionReceiver = mainActionReceiver,
    backgroundStage = backgroundStage
) {
    private val gameplayLogic = GameplayLogicImpl(soundManager, ::onAction)
    private val gameplayStage = GameplayStage(gameplayLogic, ::onAction)
    private val gameplayUIStage = GameplayUIStage(gameplayLogic) { gameplayStage.bottomUISize = it }

    private val birdsForRetroEffect = mutableListOf<BirdActor>()
    private var retroEffectTimestamp = -1f

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

        if (retroEffectTimestamp > 0) {
            ScreenUtils.clear(Color.BLACK)
            retroEffectTimestamp -= delta
            val birdIndex = (retroEffectTimestamp / RETRO_EFFECT_TIME_PER_BIRD).toInt()
            if (birdIndex < birdsForRetroEffect.size) {
                gameplayStage.drawRetroEffect(birdsForRetroEffect[birdIndex])
            }
        }
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
            is GameplayScreenAction.ShowRetroEffect -> {
                birdsForRetroEffect.clear()
                birdsForRetroEffect.addAll(action.birds)
                retroEffectTimestamp = RETRO_EFFECT_TIME_PER_BIRD * (action.birds.size + 1)
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
        private const val RETRO_EFFECT_TIME_PER_BIRD = 0.033f
    }
}
