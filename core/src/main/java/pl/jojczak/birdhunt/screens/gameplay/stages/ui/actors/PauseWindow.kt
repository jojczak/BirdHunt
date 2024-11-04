package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.base.BaseTable
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.screens.gameplay.stages.ui.GameplayUIAction
import pl.jojczak.birdhunt.utils.ButtonListener

class PauseWindow(
    i18N: I18NBundle,
    skin: Skin,
    private val gameplayUIActionReceiver: (action: GameplayUIAction) -> Unit,
    private val gameplayLogic: GameplayLogic
) : BaseTable(), GameplayLogic.FromActions {
    private val resumeButton = TextButton(i18N.get("pause_resume"), skin).also { rB ->
        rB.addListener(ButtonListener { _, _ ->
            fadeOut {
                gameplayLogic.onAction(GameplayLogic.ToActions.ResumeGame)
            }
        })
    }
    private val settingsButton = TextButton(i18N.get("bt_settings"), skin).also { sB ->
        sB.addListener(ButtonListener { _, _ ->
            gameplayUIActionReceiver(GameplayUIAction.NavigateToSettings)
        })
    }
    private val controlsButton = TextButton(i18N.get("controls"), skin).also { cB ->
        cB.addListener(ButtonListener { _, _ ->
            gameplayUIActionReceiver(GameplayUIAction.NavigateToControls)
        })
    }
    private val exitButton = TextButton(i18N.get("exit_bt"), skin).also { eB ->
        eB.addListener(ButtonListener { _, _ ->
            gameplayLogic.onAction(GameplayLogic.ToActions.ExitGame)
            eB.isDisabled = true
        })
    }

    private val window = Window(i18N.get("pause_title"), skin).also { w ->
        w.isMovable = false
        w.isResizable = false

        w.add(resumeButton).padTop(PAD).row()
        w.add(settingsButton).padTop(PAD).row()
        w.add(controlsButton).padTop(PAD).row()
        w.add(exitButton).padTop(PAD)
    }

    init {
        Gdx.app.log(TAG, "PauseWindow init")
        setFillParent(true)
        center()
        add(window)
    }

    override fun pauseStateUpdated(paused: Boolean) {
        if (paused) {
            fadeIn()
        } else {
            hide()
        }
    }

    override fun restartGame() {
        hide()
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        if (state is GameplayState.GameOver) {
            hide()
        }
    }

    companion object {
        private const val TAG = "PauseWindow"

        private const val PAD = 25f
    }
}
