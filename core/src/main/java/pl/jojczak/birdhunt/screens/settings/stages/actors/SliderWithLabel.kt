package pl.jojczak.birdhunt.screens.settings.stages.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.utils.Preferences

class SliderWithLabel(
    skin: Skin,
    private val i18n: I18NBundle,
    private val i18nKey: String,
    private val preference: Preferences.Preference<Float>,
    min: Float,
    max: Float,
    step: Float
): Table() {
    private val slider = Slider(min, max, step, false, skin).apply {
        value = Preferences.get(preference)
        addListener(SliderInputListener())
    }
    private val label = Label(i18n.format(i18nKey, "${slider.value}"), skin, Asset.FONT_46, Color.BLACK)

   init {
       add(label).expandX().fillX().padLeft(PAD_LEFT).padBottom(PAD).row()
       add(slider).expandX().fillX().row()
   }

    private inner class SliderInputListener: ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            if (slider.isDragging) {
                label.setText(i18n.format(i18nKey, slider.value))
            } else {
                Preferences.put(preference, slider.value)
            }
        }
    }

    companion object {
        private const val PAD = 10f
        private const val PAD_LEFT = 8f
    }
}
