package pl.jojczak.birdhunt.screens.mainmenu.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreenAction
import pl.jojczak.birdhunt.utils.ButtonListener

class MainMenuStage(
    private val screenActionReceiver: (action: MainMenuScreenAction) -> Unit
) : BaseUIStage() {
    private val startGameButton = TextButton(i18N.get("bt_start_game"), skin).also { sgB ->
        sgB.addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "Start button clicked")
            screenActionReceiver(MainMenuScreenAction.NavigateToGameplay)
        })
    }

    private val settingsButton = TextButton(i18N.get("bt_settings"), skin).also { sB ->
        sB.addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "Settings button clicked")
            screenActionReceiver(MainMenuScreenAction.NavigateToSettings)
        })
    }

    private val aboutButton = TextButton(i18N.get("bt_about"), skin).also { aB ->
        aB.addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "About button clicked")
            screenActionReceiver(MainMenuScreenAction.NavigateToAbout)
        })
    }

    private val containerTable = Table().also { cT ->
        cT.setFillParent(true)
        cT.center()

        cT.add(startGameButton).padBottom(ROW_PAD).row()
        cT.add(settingsButton).padBottom(ROW_PAD).row()
        cT.add(aboutButton).row()
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
