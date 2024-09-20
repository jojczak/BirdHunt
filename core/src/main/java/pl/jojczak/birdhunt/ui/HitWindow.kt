package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75
import pl.jojczak.birdhunt.screens.gameplay.ScreenGameplayHelperImpl.Companion.BIRDS_PER_ROUND

class HitWindow(
    i18N: I18NBundle,
    skin: Skin
): Window(
    i18N.get("game_label_hit"),
    skin,
    "dark"
) {
    private var hit = 0
        set(value) {
            field = value
            updateLabel()
        }

    private val hitLabel = Label("$hit$MAX_BIRDS", skin, FONT_75, Color.WHITE)

    init {
        isMovable = false
        isResizable = false

        add(hitLabel)
    }

    private fun updateLabel() {
        hitLabel.setText("$hit$MAX_BIRDS")
    }

    companion object {
        private const val MAX_BIRDS = "/${BIRDS_PER_ROUND}"
    }
}
