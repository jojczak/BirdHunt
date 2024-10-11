package pl.jojczak.birdhunt.screens.gameplay.stages.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.CountdownLabel
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.GameOverWindow
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.HitWindow
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.PauseWindow
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.RoundWindow
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.ScoreWidget
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors.ShotWindow
import pl.jojczak.birdhunt.screens.settings.SettingsScreenAction
import pl.jojczak.birdhunt.screens.settings.stages.SettingsStage
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.stageToReal

class GameplayUIStage(
    private val gameplayLogic: GameplayLogic,
    private val onBottomUiResize: (Float) -> Unit
) : BaseUIStage() {
    private val scoreWidget = ScoreWidget(i18N, skin)
    private val shotWindow = ShotWindow(i18N, skin)
    private val roundWindow = RoundWindow(i18N, skin)
    private val hitWindow = HitWindow(i18N, skin)
    private val countdownLabel = CountdownLabel(i18N, skin)
    private val pauseWindow = PauseWindow(i18N, skin, ::onAction, gameplayLogic)
    private val gameOverWindow = GameOverWindow(i18N, skin, gameplayLogic)
    private var settingsStage: SettingsStage? = null
    private val pauseButton = TextButton("||", skin).apply {
        addListener(ButtonListener { _, _ ->
            gameplayLogic.onAction(GameplayLogic.ToActions.PauseGame)
        })
    }

    private val leftGroup = Table().apply {
        add(pauseButton).size(CELL_SIZE).padLeft(PAD * 2)
        add(shotWindow).size(CELL_SIZE).padLeft(PAD * 2)
    }

    private val rightGroup = Table().apply {
        add(hitWindow).size(CELL_SIZE).padRight(PAD * 2)
        add(roundWindow).size(CELL_SIZE).padRight(PAD * 2)
    }

    private val bottomContainer = Table().apply {
        setFillParent(true)
        bottom()
        add(leftGroup).expandX().left().padBottom(PAD)
        add(rightGroup).expandX().right().padBottom(PAD)
    }

    init {
        gameplayLogic.addActionsListener(
            scoreWidget,
            shotWindow,
            roundWindow,
            hitWindow,
            countdownLabel,
            pauseWindow,
            gameOverWindow
        )

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

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        settingsStage?.onResize(scrWidth, scrHeight)

        @Suppress("UNNECESSARY_SAFE_CALL")
        onBottomUiResize?.invoke((CELL_SIZE + PAD * 2).stageToReal(this))
    }

    override fun dispose() {
        gameplayLogic.removeActionsListener(
            scoreWidget,
            shotWindow,
            roundWindow,
            hitWindow,
            countdownLabel,
            pauseWindow,
            gameOverWindow
        )
        super.dispose()
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameplayUIStage"

        const val CELL_SIZE = 190f
        const val PAD = 10f
    }
}
