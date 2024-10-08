package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75

class RoundWindow(
    i18N: I18NBundle,
    skin: Skin
): BottomUIWindow(
    i18N.get("game_label_round"),
    skin,
    "dark"
) {
    private val roundLabel = Label("1", skin, FONT_75, Color.WHITE)

    init {
        add(roundLabel)
    }
}
