package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75_BORDERED
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class ScoreWidget(
    private val i18N: I18NBundle,
    skin: Skin,
    private val gameplayHelper: GameplayHelper
): Table() {
    var score = 0
        set(value) {
            field = value
            updateScore()
        }

    private val scoreLabel = Label(i18N.format("game_label_score", score), skin, FONT_75_BORDERED, Color.WHITE)
    private val infoLabel = Label("", skin, FONT_75_BORDERED, Color.WHITE)

    init {
        gameplayHelper.addGameplayListener(GameplayEventListener())
        setFillParent(true)
        top()
        add(scoreLabel).row()
        add(infoLabel)
    }

    override fun act(delta: Float) {
        super.act(delta)
        val gameplayState = gameplayHelper.getState()

        if (gameplayState is GameplayState.Playing) {
            if (gameplayState.elapsedTime > GameplayHelper.SHOT_TIME - 1f) {
                infoLabel.setText(i18N.get("game_label_bird_time"))
            } else {
                infoLabel.setText("")
            }
        }
    }

    private fun updateScore() {
        scoreLabel.setText(i18N.format("game_label_score", scoreLabel))
    }

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun onGameplayStateChanged(state: GameplayState) {
            if (state is GameplayState.GameOver.OutOfAmmo) {
                infoLabel.setText(i18N.get("game_over_reason_ammo").uppercase())
            }
        }

        override fun reset() {
            infoLabel.setText("")
        }
    }
}
