package pl.jojczak.birdhunt.screens.feedback

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.os.helpers.osCoreHelper
import pl.jojczak.birdhunt.utils.ButtonListener

class FeedbackStage : ScreenWithUIStage.ScreenStage() {

    private val shareButton = ImageTextButton(i18N.get("bt_share"), skin, "share").also { sB ->
        sB.addListener(ButtonListener { _, _ ->
            osCoreHelper.shareApp()
        })
    }

    private val feedbackTable = Table().also { fT ->
        fT.addParagraph("feedback_label").padTop(TEXT_PAD).row()
        fT.addParagraph("feedback_reddit", "feedback_reddit_url").padTop(TEXT_PAD).row()
        fT.addParagraph("feedback_xda", "feedback_xda_url").padTop(TEXT_PAD).row()
        fT.addParagraph("feedback_youtube", "feedback_youtube_url").padTop(TEXT_PAD).row()
        fT.addParagraph("feedback_github", "feedback_github_url").padTop(TEXT_PAD).row()
        fT.addParagraph("feedback_google_play", "feedback_google_play_url").padTop(TEXT_PAD).row()
    }

    private val scrollPane = ScrollPane(feedbackTable, skin, "blank").also { sP ->
        sP.setFadeScrollBars(false)
    }

    private val feedbackWindow = Window(i18N.get("feedback_title"), skin).also { w ->
        w.isMovable = false
        w.isResizable = false
        w.add(scrollPane).expand().fill()
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
        cT.add(feedbackWindow).expandX().fillX().maxWidth(CONTAINER_MAX_WIDTH).row()
        cT.add(shareButton).padTop(CONTAINER_PAD).row()
        cT.add(backButton).padTop(CONTAINER_PAD / 3)
    }

    init {
        addActor(container)
    }

    private fun Table.addParagraph(i18NKey: String, linkUrl: String? = null): Cell<Label> {
        val color = if (linkUrl != null) Color.BLUE else Color.BLACK
        val label = Label(i18N.get(i18NKey), this@FeedbackStage.skin, Asset.FONT_SMALL, color).also { label ->
            label.wrap = true
            linkUrl?.let {
                label.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        Gdx.net.openURI(i18N.get(linkUrl))
                    }
                })
                label.setAlignment(Align.center)
            }
        }
        return add(label).expandX().fillX()
    }

    override fun keyDown(keyCode: Int) = if (keyCode == Keys.BACK) {
        fadeOut {
            mainActionReceiver(MainAction.NavigateToMainMenu)
        }
        true
    } else super.keyDown(keyCode)

    companion object {
        private const val TEXT_PAD = 25f
        private const val CONTAINER_PAD = 60f
        private const val CONTAINER_MAX_WIDTH = WORLD_WIDTH - CONTAINER_PAD * 2
    }
}
