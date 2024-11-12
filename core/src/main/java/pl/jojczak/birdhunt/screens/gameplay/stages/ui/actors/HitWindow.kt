package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_MEDIUM
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic

class HitWindow(
    i18N: I18NBundle,
    skin: Skin
) : BottomUIWindow(
    i18N.get("game_label_hit"),
    skin,
    "dark"
), GameplayLogic.FromActions {
    private val hitLabel = Label(
        "${GameplayLogic.DEF_HIT}/${GameplayLogic.HITS_PER_ROUND}",
        skin,
        FONT_MEDIUM,
        Color.WHITE
    )

    init {
        add(hitLabel)
    }

    override fun hitUpdated(hit: Int) {
        hitLabel.setText("$hit/${GameplayLogic.HITS_PER_ROUND}")
    }
}
