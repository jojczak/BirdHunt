package pl.jojczak.birdhunt.screens.mainmenu.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreenAction
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.PREF_HIGH_SCORE
import pl.jojczak.birdhunt.utils.PREF_NAME
import pl.jojczak.birdhunt.utils.appVersion

class MainMenuStage(
    private val screenActionReceiver: (action: MainMenuScreenAction) -> Unit
) : BaseUIStage() {
    private var orientationVertical: Boolean? = null
    private val preferences = Gdx.app.getPreferences(PREF_NAME)

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

    private val highScoreLabel = Label(
        i18N.format("main_menu_high_score", PREF_HIGH_SCORE.getInt(preferences)),
        skin,
        Asset.FONT_75_BORDERED,
        Color.WHITE
    )

    private val infoLabel = Label(
        i18N.format("main_menu_info", appVersion),
        skin,
        Asset.FONT_46_BORDERED,
        Color.WHITE
    ).also { iL ->
        iL.setPosition(ROW_PAD, ROW_PAD)
    }

    private val logoActor = Image(AssetsLoader.get<Texture>(Asset.TX_LOGO)).also { lA ->
        lA.setScaling(Scaling.fit)
        lA.align = Align.top
    }

    private var currentTable: Table? = null

    init {
        Gdx.app.log(TAG, "init MainMenuStage")
        addActor(infoLabel)
    }

    override fun onFirstFrame() {
        super.onFirstFrame()
        screenActionReceiver(MainMenuScreenAction.FirstFrameDrawn)
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)

        if (logoActor == null) return

        if (viewport.worldHeight > viewport.worldWidth && orientationVertical != true) {
            Gdx.app.log(TAG, "Orientation changed to vertical")
            orientationVertical = true
            currentTable?.remove()
            currentTable = getVerticalTable()
            addActor(currentTable)

        } else if (viewport.worldWidth > viewport.worldHeight && orientationVertical != false) {
            Gdx.app.log(TAG, "Orientation changed to horizontal")
            orientationVertical = false
            currentTable?.remove()
            currentTable = getHorizontalTable()
            addActor(currentTable)
        }
    }

    private fun getHorizontalTable() = Table().also { cT ->
        cT.setFillParent(true)
        cT.center().top()

        cT.add(logoActor).prefSize(
            logoActor.drawable.minWidth * 5f,
            logoActor.drawable.minHeight * 5f
        ).expandX().align(Align.top)

        cT.add().expandX()

        cT.add(Table().also { bT ->
            bT.add(startGameButton).padBottom(ROW_PAD).row()
            bT.add(settingsButton).padBottom(ROW_PAD).row()
            bT.add(aboutButton).padBottom(ROW_PAD).row()
            bT.add(highScoreLabel).row()
        }).expand().center().padRight(50f)
    }

    private fun getVerticalTable() = Table().also { cT ->
        cT.setFillParent(true)
        cT.center().top()

        cT.add(logoActor).prefSize(
            logoActor.drawable.minWidth * 5f,
            logoActor.drawable.minHeight * 5f
        ).expandY().align(Align.top).row()

        cT.add(Table().also { bT ->
            bT.pad(ROW_PAD * 8, 0f, ROW_PAD * 8, 0f)
            bT.add(startGameButton).padBottom(ROW_PAD).row()
            bT.add(settingsButton).padBottom(ROW_PAD).row()
            bT.add(aboutButton).padBottom(ROW_PAD).row()
            bT.add(highScoreLabel).row()
        }).expandY().align(Align.top).row()

        cT.add().minHeight(50f).expandY()
    }

    companion object {
        private const val TAG = "MainMenuStage"

        private const val ROW_PAD = 15f
    }
}
