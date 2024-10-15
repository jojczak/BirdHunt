package pl.jojczak.birdhunt.screens.unsupporteddevice.stages

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.Preferences

class UnsupportedDeviceStage: ScreenWithUIStage.ScreenStage() {

    private val text = Label(i18N.get("unsupported_device_content"), skin, Asset.FONT_46, Color.BLACK).also { t ->
        t.wrap = true
    }

    private val window = Window(i18N.get("unsupported_device_title"), skin).also { w ->
        w.isMovable = false
        w.isResizable = false

        w.top()
        w.add(text).padTop(PAD).expand().fill()
    }

    private val button = TextButton(i18N.get("unsupported_device_button"), skin).also { b ->
        b.addListener(ButtonListener { _, _ ->
            fadeOut {
                if (Preferences.get(Preferences.PREF_FIRST_RUN)) {
                    Preferences.put(Preferences.PREF_FIRST_RUN, false)
                    Preferences.flush()
                    mainActionReceiver(MainAction.NavigateToAbout)
                } else {
                    mainActionReceiver(MainAction.NavigateToMainMenu)
                }
            }
        })
    }

    private val container = Table().also { cT ->
        cT.setFillParent(true)
        cT.add(window).row()
        cT.add(button).padTop(CONTAINER_PAD)
    }

    init {
        addActor(container)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "UnsupportedDeviceStage"

        private const val PAD = 10f
        private const val CONTAINER_PAD = 60f
    }
}
