package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75_BORDERED
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class ScoreWidget(
    private val i18N: I18NBundle,
    skin: Skin
): Table(), GameplayLogic.FromActions {
    private val scoreLabel = Label(i18N.format("game_label_score", GameplayLogic.DEF_POINTS), skin, FONT_75_BORDERED, Color.WHITE)
    private val infoLabel = Label("", skin, FONT_75_BORDERED, Color.WHITE)

    init {
        setFillParent(true)
        top()
        add(scoreLabel).row()
        add(infoLabel)
    }

    override fun pointsUpdated(points: Int) {
        scoreLabel.setText(i18N.format("game_label_score", points))
    }

    override fun displayWarning(state: GameplayState.GameOver?) {
        when (state) {
            is GameplayState.GameOver.OutOfAmmo -> infoLabel.setText(i18N.get("game_over_reason_ammo"))
            is GameplayState.GameOver.OutOfTime -> infoLabel.setText(i18N.get("game_label_bird_time"))
            null -> infoLabel.setText("")
        }
    }
}
