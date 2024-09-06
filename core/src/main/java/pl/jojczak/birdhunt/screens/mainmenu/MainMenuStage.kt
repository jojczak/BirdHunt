package pl.jojczak.birdhunt.screens.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import pl.jojczak.birdhunt.base.BaseUIStage

class MainMenuStage(
    private val screenActionReceiver: (action: MainMenuScreenAction) -> Unit
) : BaseUIStage() {
    private val startGameButton = TextButton(i18n.get("bt_start_game"), skin).apply {
        addListener(StartButtonClickListener())
    }

    private val settingsButton = TextButton(i18n.get("bt_settings"), skin)
    private val aboutButton = TextButton(i18n.get("bt_about"), skin)

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

    private inner class StartButtonClickListener : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            Gdx.app.log(TAG, "Start button clicked")
            screenActionReceiver(MainMenuScreenAction.StartGame)
        }
    }

    companion object {
        private const val TAG = "MainMenuStage"

        private const val ROW_PAD = 15f
    }
}
