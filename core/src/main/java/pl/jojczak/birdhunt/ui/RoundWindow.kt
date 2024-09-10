package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75

class RoundWindow(
    i18N: I18NBundle,
    skin: Skin
): Window(
    i18N.get("game_label_round"),
    skin,
    "dark"
) {
    private var round = 1
        set(value) {
            field = value
            updateLabel()
        }

    private val roundLabel = Label("$round", skin, FONT_75, Color.WHITE)

    init {
        isMovable = false
        isResizable = false

        add(roundLabel)
    }

    private fun updateLabel() {
        roundLabel.setText("$round")
    }
}
