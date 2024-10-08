package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75

class HitWindow(
    i18N: I18NBundle,
    skin: Skin
) : BottomUIWindow(
    i18N.get("game_label_hit"),
    skin,
    "dark"
) {
    private var hit = 0
        set(value) {
            field = value
            hitLabel.setText("$hit")
        }

    private val hitLabel = Label("0", skin, FONT_75, Color.WHITE)

    init {
        add(hitLabel)
    }
}
