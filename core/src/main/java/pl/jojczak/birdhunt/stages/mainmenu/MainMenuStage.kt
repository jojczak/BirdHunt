package pl.jojczak.birdhunt.stages.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreenAction
import pl.jojczak.birdhunt.utils.ButtonListener

class MainMenuStage(
    private val screenActionReceiver: (action: MainMenuScreenAction) -> Unit
) : BaseUIStage() {
    private val startGameButton = TextButton(i18N.get("bt_start_game"), skin).apply {
        addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "Start button clicked")
            screenActionReceiver(MainMenuScreenAction.NavigateToGameplay)
        })
    }

    private val settingsButton = TextButton(i18N.get("bt_settings"), skin).apply {
        addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "Settings button clicked")
            screenActionReceiver(MainMenuScreenAction.NavigateToSettings)
        })
    }

    private val aboutButton = TextButton(i18N.get("bt_about"), skin)

    private val containerTable = Table().apply {
        setFillParent(true)
        center()

        add(startGameButton).padBottom(ROW_PAD).row()
        add(settingsButton).padBottom(ROW_PAD).row()
        add(aboutButton).row()
    }

    init {
        Gdx.app.log(TAG, "init MainMenuStage")
        addActor(containerTable)
    }

    companion object {
        private const val TAG = "MainMenuStage"

        private const val ROW_PAD = 15f
    }
}
