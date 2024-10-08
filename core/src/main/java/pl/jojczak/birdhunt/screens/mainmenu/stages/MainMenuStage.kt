package pl.jojczak.birdhunt.screens.mainmenu.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreenAction
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.appVersion

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

    private val infoLabel = Label(
        i18N.format("main_menu_info", appVersion),
        skin,
        Asset.FONT_46_BORDERED,
        Color.WHITE
    ).also { iL ->
        iL.setPosition(ROW_PAD, ROW_PAD)
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
        addActor(infoLabel)
    }

    companion object {
        private const val TAG = "MainMenuStage"

        private const val ROW_PAD = 15f
    }
}
