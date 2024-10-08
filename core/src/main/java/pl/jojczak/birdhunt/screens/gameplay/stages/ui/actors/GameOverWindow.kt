package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.base.BaseTable
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.utils.ButtonListener

class GameOverWindow(
    i18N: I18NBundle,
    skin: Skin,
    private val gameplayLogic: GameplayLogic
) : BaseTable(), GameplayLogic.FromActions {
    private val restartButton = TextButton(i18N.get("game_over_bt_restart"), skin).apply {
        addListener(ButtonListener { _, _ ->
            fadeOut {
                gameplayLogic.onAction(GameplayLogic.ToActions.RestartGame)
            }
        })
    }
    private val exitButton = TextButton(i18N.get("exit_bt"), skin).apply {
        addListener(ButtonListener { _, _ ->
            gameplayLogic.onAction(GameplayLogic.ToActions.ExitGame)
            isDisabled = true
        })
    }

    private val window = Window(i18N.get("game_over"), skin).apply {
        isMovable = false
        isResizable = false

        add(restartButton).padTop(PAD).row()
        add(exitButton).padTop(PAD)
    }

    init {
        setFillParent(true)
        center()
        add(window)
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        if (state is GameplayState.GameOver) {
            fadeIn()
        } else (
            hide()
        )
    }

    override fun restartGame() {
        hide()
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameOverWindow"

        private const val PAD = 25f
    }
}
