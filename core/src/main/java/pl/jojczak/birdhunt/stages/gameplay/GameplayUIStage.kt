package pl.jojczak.birdhunt.stages.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import pl.jojczak.birdhunt.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.screens.settings.SettingsScreenAction
import pl.jojczak.birdhunt.stages.settings.SettingsStage
import pl.jojczak.birdhunt.ui.CountdownLabel
import pl.jojczak.birdhunt.ui.GameOverWindow
import pl.jojczak.birdhunt.ui.HitWindow
import pl.jojczak.birdhunt.ui.PauseWindow
import pl.jojczak.birdhunt.ui.RoundWindow
import pl.jojczak.birdhunt.ui.ScoreWidget
import pl.jojczak.birdhunt.ui.ShotWindow
import pl.jojczak.birdhunt.utils.ButtonListener

class GameplayUIStage(
    private val gameplayHelper: GameplayHelper
) : BaseUIStage() {
    private val scoreWidget = ScoreWidget(i18N, skin, gameplayHelper)
    private val shotWindow = ShotWindow(i18N, skin, gameplayHelper)
    private val roundWindow = RoundWindow(i18N, skin, gameplayHelper)
    private val hitWindow = HitWindow(i18N, skin, gameplayHelper)
    private val countdownLabel = CountdownLabel(i18N, skin, gameplayHelper)
    private val pauseWindow = PauseWindow(i18N, skin, ::onAction, gameplayHelper)
    private val gameOverWindow = GameOverWindow(i18N, skin, gameplayHelper)
    private var settingsStage: SettingsStage? = null
    private val pauseButton = TextButton("||", skin).apply {
        addListener(ButtonListener { _, _ ->
            gameplayHelper.action(GameplayHelper.GameplayAction.PauseGame)
        })
    }

    private val leftGroup = Table().apply {
        add(pauseButton).size(CELL_SIZE).padLeft(PAD)
        add(shotWindow).size(CELL_SIZE).padLeft(PAD)
    }

    private val rightGroup = Table().apply {
        add(hitWindow).size(CELL_SIZE).padRight(PAD)
        add(roundWindow).size(CELL_SIZE).padRight(PAD)
    }

    private val bottomContainer = Table().apply {
        val bottomPad = (ShotgunActor.FRAME_HEIGHT.toGameSize() - CELL_SIZE) / 2f

        setFillParent(true)
        bottom()
        add(leftGroup).expandX().left().padBottom(bottomPad)
        add(rightGroup).expandX().right().padBottom(bottomPad)
    }

    init {
        gameplayHelper.addGameplayListener(GameplayEventListener())
        addActor(scoreWidget)
        addActor(bottomContainer)
        addActor(countdownLabel)
        addActor(pauseWindow)
        addActor(gameOverWindow)
    }

    override fun act(delta: Float) {
        super.act(delta)
        settingsStage?.act(delta)
    }

    override fun draw() {
        super.draw()
        settingsStage?.draw()
    }

    private fun onAction(action: GameplayUIAction) {
        when (action) {
            GameplayUIAction.NavigateToSettings -> {
                settingsStage = SettingsStage(false, ::onSettingsStageAction).also { sS ->
                    sS.fadeIn()
                }
                Gdx.app.input.inputProcessor = settingsStage
                pauseWindow.fadeOut()
            }
        }
    }

    private fun onSettingsStageAction(action: SettingsScreenAction) {
        when (action) {
            SettingsScreenAction.NavigateToMainMenu -> {
                Gdx.app.input.inputProcessor = this
                pauseWindow.fadeIn()
                settingsStage?.fadeOut {
                    settingsStage?.dispose()
                    settingsStage = null
                }
            }
        }
    }

    inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun onGameplayStateChanged(state: GameplayState) {
            pauseButton.isDisabled = state is GameplayState.Paused
        }
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        settingsStage?.onResize(scrWidth, scrHeight)
    }

    companion object {
        private const val TAG = "GameplayUIStage"

        private const val CELL_SIZE = 190f
        private const val PAD = 20f
    }
}
