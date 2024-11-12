package pl.jojczak.birdhunt.screens.gameplay.stages.howtoplay

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.utils.ButtonListener

class ControlsStage(
    private val firstGame: Boolean = false
) : ScreenWithUIStage.ScreenStage() {

    private val controlsTable = Table().also { cT ->
        val sPenImage = Image(AssetsLoader.get<Texture>(Asset.TX_CONTROL_S_PEN))
        val touchImage = Image(AssetsLoader.get<Texture>(Asset.TX_CONTROL_TOUCH))

        cT.add(Label(i18N.get("controls_s_pen"), skin, Asset.FONT_MEDIUM, Color.BLACK)).padTop(PAD)
        cT.add(Label(i18N.get("controls_touch"), skin, Asset.FONT_MEDIUM, Color.BLACK)).padTop(PAD).row()
        cT.add(sPenImage).size(sPenImage.prefWidth * 6f, sPenImage.prefHeight * 6f).padTop(PAD).padLeft(PAD).padRight(PAD / 2)
        cT.add(touchImage).size(touchImage.prefWidth * 6f, touchImage.prefHeight * 6f).padTop(PAD).padLeft(PAD / 2).padRight(PAD).row()
        cT.add(Label(i18N.get("controls_s_pen_desc"), skin, Asset.FONT_SMALL, Color.BLACK).apply { wrap = true }).fillX().expandX().padTop(PAD).padRight(PAD)
        cT.add(Label(i18N.get("controls_touch_desc"), skin, Asset.FONT_SMALL, Color.BLACK).apply { wrap = true }).fillX().expandX().padTop(PAD).padLeft(PAD).top()
    }

    private val scrollPane = ScrollPane(controlsTable, skin, "blank").also { sP ->
        sP.setFadeScrollBars(false)
    }

    private val window = Window(i18N.get("controls"), skin).also { w ->
        w.isMovable = false
        w.isResizable = false
        w.top()
        w.add(scrollPane).expand().fill()
    }

    private val backButton = TextButton(i18N.get("back_bt"), skin).also { bB ->
        bB.addListener(ButtonListener { _, _ ->
            fadeOut {
                mainActionReceiver(MainAction.NavigateToMainMenu)
            }
        })
    }

    private val startGameButton = TextButton(i18N.get("bt_start_game"), skin).also { bSG ->
        bSG.addListener(ButtonListener { _, _ ->
            fadeOut {
                mainActionReceiver(MainAction.NavigateToGameplay)
            }
        })
    }

    private val container = Table().also { cT ->
        cT.setFillParent(true)
        cT.add(window).maxWidth(CONTAINER_MAX_WIDTH).row()

        if (firstGame) {
            cT.add(startGameButton).padTop(PAD * 1.5f)
        } else {
            cT.add(backButton).padTop(PAD * 1.5f)
        }
    }

    init {
        addActor(container)
    }

    override fun keyDown(keyCode: Int) = if (keyCode == Keys.BACK) {
        fadeOut {
            mainActionReceiver(MainAction.NavigateToMainMenu)
        }
        true
    } else super.keyDown(keyCode)

    companion object {
        private const val PAD = 40f
        private const val CONTAINER_MAX_WIDTH = WORLD_WIDTH - PAD
    }
}
