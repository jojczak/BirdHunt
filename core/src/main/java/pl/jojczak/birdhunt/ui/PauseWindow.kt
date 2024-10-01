package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.base.BaseTable
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.stages.gameplay.GameplayUIAction
import pl.jojczak.birdhunt.utils.ButtonListener

class PauseWindow(
    i18N: I18NBundle,
    skin: Skin,
    private val gameplayUIActionReceiver: (action: GameplayUIAction) -> Unit,
    private val gameplayHelper: GameplayHelper
) : BaseTable() {
    private val resumeButton = TextButton(i18N.get("pause_resume"), skin).also { rB ->
        rB.addListener(ButtonListener { _, _ ->
            fadeOut { gameplayHelper.action(GameplayHelper.GameplayAction.ResumeGame) }
        })
    }
    private val settingsButton = TextButton(i18N.get("bt_settings"), skin).also { sB ->
        sB.addListener(ButtonListener { _, _ ->
            gameplayUIActionReceiver(GameplayUIAction.NavigateToSettings)
        })
    }
    private val exitButton = TextButton(i18N.get("exit_bt"), skin).also { eB ->
        eB.addListener(ButtonListener { _, _ ->
            eB.isDisabled = true
            gameplayHelper.action(GameplayHelper.GameplayAction.ExitGame)
        })
    }

    private val window = Window(i18N.get("pause_title"), skin).also { w ->
        w.isMovable = false
        w.isResizable = false

        w.add(resumeButton).padTop(PAD).row()
        w.add(settingsButton).padTop(PAD).row()
        w.add(exitButton).padTop(PAD)
    }

    init {
        Gdx.app.log(TAG, "PauseWindow init")
        gameplayHelper.addGameplayListener(GameplayEventListener())
        setFillParent(true)
        center()
        add(window)
    }

    inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun onGameplayStateChanged(state: GameplayState) {
            if (state is GameplayState.Paused) fadeIn()
        }
    }

    companion object {
        private const val TAG = "PauseWindow"

        private const val PAD = 25f
    }
}
