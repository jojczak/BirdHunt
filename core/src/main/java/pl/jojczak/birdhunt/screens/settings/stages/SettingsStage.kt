package pl.jojczak.birdhunt.screens.settings.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.settings.SettingsScreenAction
import pl.jojczak.birdhunt.utils.ButtonListener
import pl.jojczak.birdhunt.utils.PREF_NAME
import pl.jojczak.birdhunt.utils.PREF_SENSITIVITY
import pl.jojczak.birdhunt.utils.PREF_SOUND
import java.util.Locale

class SettingsStage(
    private val isFromMainMenu: Boolean,
    private val settingsScreenActionReceiver: (action: SettingsScreenAction) -> Unit
) : BaseUIStage() {
    private val preferences = Gdx.app.getPreferences(PREF_NAME)

    private val sensitivityLabel = Label("", skin, Asset.FONT_46, Color.BLACK)
    private val sensitivitySlider = Slider(1f, 40f, 0.1f, false, skin).also { sS ->
        sS.addListener(SliderChangeListener())
    }

    private val soundCheckBox = CheckBox(i18N.get("settings_sound_checkbox"), skin).also { sCB ->
        sCB.addListener(ButtonListener { _, _ ->
            PREF_SOUND.putBoolean(preferences, sCB.isChecked)
        })
    }

    private val settingsWindow = Window(i18N.get("pause_title"), skin).also { sW ->
        sW.isMovable = false
        sW.isResizable = false
        sW.top()

        sW.add(sensitivityLabel).padTop(SETTINGS_ITEM_PAD).padLeft(SETTINGS_ITEM_PAD / 2).left().row()
        sW.add(sensitivitySlider).expandX().padTop(SETTINGS_ITEM_PAD).fillX().row()
        sW.add(soundCheckBox).padTop(SETTINGS_ITEM_PAD).padLeft(SETTINGS_ITEM_PAD / 2).left()
    }

    private val backButton = TextButton(i18N.get("back_bt"), skin).also { bB ->
        bB.addListener(ButtonListener { _, _ ->
            preferences.flush()
            if (isFromMainMenu) fadeOut {
                settingsScreenActionReceiver(SettingsScreenAction.NavigateToMainMenu)
            } else {
                settingsScreenActionReceiver(SettingsScreenAction.NavigateToMainMenu)
            }
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
        sensitivitySlider.setValue(PREF_SENSITIVITY.getFloat(preferences))
        soundCheckBox.isChecked = PREF_SOUND.getBoolean(preferences)
        updateSensitivityLabel()
    }

    private fun updateSensitivityLabel() {
        sensitivityLabel.setText(
            i18N.format(
                "settings_sensitivity_label",
                String.format(Locale.getDefault(), "%.1f", sensitivitySlider.value)
            )
        )
    }

    private inner class SliderChangeListener: ChangeListener(){
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            Gdx.app.log(TAG, "Sensitivity changed to ${sensitivitySlider.value}")
            PREF_SENSITIVITY.putFloat(preferences, sensitivitySlider.value)
            updateSensitivityLabel()
        }
    }

    companion object {
        private const val TAG = "SettingsStage"

        private const val SETTINGS_ITEM_PAD = 20f
        private const val CONTAINER_PAD = 60f
        private const val CONTAINER_MAX_WIDTH = WORLD_WIDTH - CONTAINER_PAD * 2
    }
}
