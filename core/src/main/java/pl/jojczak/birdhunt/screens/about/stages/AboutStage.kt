package pl.jojczak.birdhunt.screens.about.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.utils.ButtonListener

class AboutStage: ScreenWithUIStage.ScreenStage() {
    private val aboutTable = Table().also { aT ->
        aT.add(getParagraph("about_p1")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p2")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p3")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(Image(AssetsLoader.get<Texture>(Asset.UI_S_PEN))).row()
        aT.add(getParagraph("about_p4")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p5")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p6")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p7")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p7b", true)).fillX().expandX().padTop(PR_PAD / 2).row()
        aT.add(getParagraph("about_p8")).fillX().expandX().padTop(PR_PAD).row()
        aT.add(getParagraph("about_p8b", true)).fillX().expandX().padTop(PR_PAD / 2).row()
        aT.add(getParagraph("about_p9")).fillX().expandX().padTop(PR_PAD).row()
    }

    private val scrollPane = ScrollPane(aboutTable, skin, "blank").also { sP ->
        sP.setFadeScrollBars(false)
    }

    private val aboutWindow = Window(i18N.get("about_label"), skin).also { sW ->
        sW.isMovable = false
        sW.isResizable = false
        sW.add(scrollPane).expand().fill()
    }

    private val backButton = TextButton(i18N.get("back_bt"), skin).also { bB ->
        bB.addListener(ButtonListener { _, _ ->
            fadeOut {
                mainActionReceiver(MainAction.NavigateToMainMenu)
            }
        })
    }

    private val container = Table().also { cT ->
        cT.setFillParent(true)
        cT.pad(CONTAINER_PAD)
        cT.add(aboutWindow).expand().fill().maxWidth(CONTAINER_MAX_WIDTH).row()
        cT.add(backButton).padTop(CONTAINER_PAD)
    }

    init {
        addActor(container)
    }

    private fun getParagraph(i18NKey: String, urlLink: Boolean = false): Label {
        return if (urlLink) {
            Label(i18N.get(i18NKey), skin, Asset.FONT_46, Color.BLUE).also { l ->
                l.wrap = true
                l.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        Gdx.net.openURI(i18N.get(i18NKey))
                    }
                })
            }
        } else Label(i18N.get(i18NKey), skin, Asset.FONT_46, Color.BLACK).also { l ->
            l.wrap = true
        }
    }

    override fun keyDown(keyCode: Int) = if (keyCode == Keys.BACK) {
        mainActionReceiver(MainAction.NavigateToMainMenu)
        true
    } else super.keyDown(keyCode)

    companion object {
        @Suppress("unused")
        private const val TAG = "SettingsStage"

        private const val PR_PAD = 25f
        private const val CONTAINER_PAD = 60f
        private const val CONTAINER_MAX_WIDTH = WORLD_WIDTH - CONTAINER_PAD * 2
    }
}
