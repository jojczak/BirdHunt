package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_MEDIUM
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic

class RoundWindow(
    i18N: I18NBundle,
    skin: Skin
): BottomUIWindow(
    i18N.get("game_label_round"),
    skin,
    "dark"
), GameplayLogic.FromActions {
    private val roundLabel = Label("${GameplayLogic.DEF_ROUND}", skin, FONT_MEDIUM, Color.WHITE)

    init {
        add(roundLabel)
    }

    override fun roundUpdated(round: Int) {
        addAction(getFlashingAnim(roundLabel) {
            roundLabel.setText("$round")
        })
    }

    override fun restartGame() {
        removeFlashingAnim()
    }
}
