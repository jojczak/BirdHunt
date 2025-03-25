package pl.jojczak.birdhunt.screens.settings.stages

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.settings.stages.actors.SliderWithLabel
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_GAME_SCALE
import pl.jojczak.birdhunt.utils.Preferences.PREF_PGS_AUTH
import pl.jojczak.birdhunt.utils.Preferences.PREF_SENSITIVITY
import pl.jojczak.birdhunt.utils.Preferences.PREF_SOUND
import pl.jojczak.birdhunt.os.helpers.playServicesHelperInstance
import pl.jojczak.birdhunt.utils.Preferences.PREF_SCREEN_FLASHING

class SettingsStage : ScreenWithUIStage.ScreenStage() {

    private val pgsPreferenceListener = Preferences.PreferenceListener<Boolean> { isAuthenticated ->
        if (isAuthenticated) pgsSignInButton.remove()
    }

    private val sensitivitySlider = SliderWithLabel(
        skin = skin,
        i18n = i18N,
        i18nKey = "settings_sensitivity_label",
        preference = PREF_SENSITIVITY,
        min = 1f,
        max = 60f,
        step = 0.1f
    )

    private val gameScaleSlider = SliderWithLabel(
        skin = skin,
        i18n = i18N,
        i18nKey = "settings_game_scale_label",
        preference = PREF_GAME_SCALE,
        min = 1f,
        max = 2f,
        step = 0.5f
    )

    private val soundCheckBox = CheckBox(i18N.get("settings_sound_checkbox"), skin).also { sCB ->
        sCB.addListener(ButtonListener { _, _ ->
            Preferences.put(PREF_SOUND, sCB.isChecked)
        })
    }

    private val screenFlashingCheckBox = CheckBox(i18N.get("settings_screen_flashing"), skin).also { sCB ->
        sCB.addListener(ButtonListener { _, _ ->
            Preferences.put(PREF_SCREEN_FLASHING, sCB.isChecked)
        })
    }

    private val pgsSignInButton = TextButton(i18N.get("settings_pgs_button"), skin, "small").also { pgsB ->
        pgsB.addListener(ButtonListener { _, _ ->
            playServicesHelperInstance.signIn()
        })
    }

    private val settingsWindow = Window(i18N.get("settings_label"), skin).also { sW ->
        sW.isMovable = false
        sW.isResizable = false
        sW.top()

        sW.add(sensitivitySlider).padTop(SETTINGS_ITEM_PAD).expandX().fillX().row()
        sW.add(gameScaleSlider).padTop(SETTINGS_ITEM_PAD).expandX().fillX().row()
        sW.add(soundCheckBox).padTop(SETTINGS_ITEM_PAD * 1.5f).padLeft(SETTINGS_ITEM_PAD / 2).left().row()
        sW.add(screenFlashingCheckBox).padTop(SETTINGS_ITEM_PAD * 1.5f).padLeft(SETTINGS_ITEM_PAD / 2).left().row()

        if (!Preferences.get(PREF_PGS_AUTH)) {
            sW.add(pgsSignInButton).padTop(SETTINGS_ITEM_PAD * 1.5f).row()
        }
    }

    private val backButton = TextButton(i18N.get("back_bt"), skin).also { bB ->
        bB.addListener(ButtonListener { _, _ ->
            Preferences.flush()
            fadeOut { mainActionReceiver(MainAction.NavigateToMainMenu) }
        })
    }

    private val container = Table().also { cT ->
        cT.setFillParent(true)
        cT.pad(CONTAINER_PAD)
        cT.add(settingsWindow).expand().fill().maxWidth(CONTAINER_MAX_WIDTH).row()
        cT.add(backButton).padTop(CONTAINER_PAD)
    }

    init {
        addActor(container)
        soundCheckBox.isChecked = Preferences.get(PREF_SOUND)
        screenFlashingCheckBox.isChecked = Preferences.get(PREF_SCREEN_FLASHING)
        Preferences.addListener(PREF_PGS_AUTH, pgsPreferenceListener)
    }

    override fun keyDown(keyCode: Int) = if (keyCode == Keys.BACK) {
        fadeOut {
            Preferences.flush()
            mainActionReceiver(MainAction.NavigateToMainMenu)
        }
        true
    } else super.keyDown(keyCode)

    override fun dispose() {
        Preferences.removeListener(PREF_PGS_AUTH, pgsPreferenceListener)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "SettingsStage"

        private const val SETTINGS_ITEM_PAD = 20f
        private const val CONTAINER_PAD = 60f
        private const val CONTAINER_MAX_WIDTH = WORLD_WIDTH - CONTAINER_PAD * 2
    }
}
