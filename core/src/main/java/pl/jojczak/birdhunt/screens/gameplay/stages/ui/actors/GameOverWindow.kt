package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Scaling
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseTable
import pl.jojczak.birdhunt.os.helpers.osCoreHelper
import pl.jojczak.birdhunt.os.helpers.playServicesHelperInstance
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.utils.ButtonListener
import java.io.File
import java.util.zip.Deflater

class GameOverWindow(
    private val i18N: I18NBundle,
    skin: Skin,
    private val gameplayLogic: GameplayLogic
) : BaseTable(), GameplayLogic.FromActions {
    private val gameLogo = Image(AssetsLoader.get<Texture>(Asset.TX_LOGO)).also { lA ->
        lA.setScaling(Scaling.fit)
        lA.align = Align.top
    }

    private val gameOverLabel = Label("", skin, Asset.FONT_MEDIUM_BORDERED, Color.WHITE).apply {
        setAlignment(Align.center)
    }

    private val restartButton = TextButton(i18N.get("game_over_bt_restart"), skin).apply {
        addListener(ButtonListener { _, _ ->
            fadeOut {
                gameplayLogic.onAction(GameplayLogic.ToActions.RestartGame)
            }
        })
    }

    private val shareButton = ImageTextButton(i18N.get("bt_share"), skin, "share").apply {
        addListener(ButtonListener { _, _ ->
            Gdx.app.log(TAG, "Share button clicked")
            val screenShotFile = takeAScreenshot()
            osCoreHelper.shareAppWithScreenshot(screenShotFile)
        })
    }

    private val exitButton = TextButton(i18N.get("exit_bt"), skin).apply {
        addListener(ButtonListener { _, _ ->
            gameplayLogic.onAction(GameplayLogic.ToActions.ExitGame)
            isDisabled = true
        })
    }

    private val window = Window(i18N.get("game_over"), skin).apply {
        isMovable = false
        isResizable = false

        add(restartButton).padTop(PAD).row()
        add(shareButton).padTop(PAD).row()
        add(exitButton).padTop(PAD)

        align(Align.top)
    }

    init {
        setFillParent(true)
        onOrientationChange(isOrientationVertical)
    }

    override fun onOrientationChange(vertical: Boolean) {
        clearChildren()

        if (vertical) {
            add(gameLogo).adjustGameLogo().row()
            add(gameOverLabel).padTop(PAD).row()
            add(window).padTop(PAD).expandY().align(Align.top)
        } else {
            add(gameLogo).adjustGameLogo().expand().uniformX()
            add(window)
            add(gameOverLabel).expandX().uniformX()
        }
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        if (state is GameplayState.GameOver) {
            playServicesHelperInstance.getUserName {
                val userName = it ?: DEF_PLAYER_NAME

                var text = if (state.killedBirds < 2)
                    i18N.format("game_over_text1a", userName, state.killedBirds)
                else
                    i18N.format("game_over_text1b", userName, state.killedBirds)

                text += "\n"

                text += if (state.firedShots < 2)
                    i18N.format("game_over_text2a", state.firedShots)
                else
                    i18N.format("game_over_text2b", state.firedShots)

                text += "\n"

                text += i18N.format("game_over_text3", state.points)

                text += when (state) {
                    is GameplayState.GameOver.OutOfTime -> i18N.get("game_over_text4a")
                    is GameplayState.GameOver.OutOfAmmo -> i18N.get("game_over_text4b")
                }

                gameOverLabel.setText(text)
            }

            fadeIn()
        } else (
            hide()
        )
    }

    override fun restartGame() {
        hide()
    }

    private fun <T : Actor> Cell<T>.adjustGameLogo(): Cell<T> {
        return this
            .prefSize(
                gameLogo.drawable.minWidth * GAME_LOGO_SCALE,
                gameLogo.drawable.minHeight * GAME_LOGO_SCALE
            )
            .align(Align.center or Align.top)
    }

    private fun takeAScreenshot(): File {
        val pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.width, Gdx.graphics.height)

        val fileHandle = Gdx.files.external("screenshot.png")
        PixmapIO.writePNG(fileHandle, pixmap, Deflater.DEFAULT_COMPRESSION, true)
        pixmap.dispose()

        return File(fileHandle.file().absolutePath)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameOverWindow"

        private const val PAD = 40f
        private const val DEF_PLAYER_NAME = "Player"
        private const val GAME_LOGO_SCALE = 3f
    }
}
