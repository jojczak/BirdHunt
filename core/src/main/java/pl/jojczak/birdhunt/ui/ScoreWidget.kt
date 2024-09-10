package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75_BORDERED

class ScoreWidget(
    private val i18N: I18NBundle,
    skin: Skin
): Table() {
    var score = 0
        set(value) {
            field = value
            updateScore()
        }

    private val scoreLabel = Label(i18N.format("game_label_score", score), skin, FONT_75_BORDERED, Color.WHITE)

    init {
        setFillParent(true)
        top()
        add(scoreLabel)
    }

    private fun updateScore() {
        scoreLabel.setText(i18N.format("game_label_score", scoreLabel))
    }
}
