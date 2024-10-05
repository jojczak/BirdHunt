package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_180_BORDERED
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class CountdownLabel(
    private val i18N: I18NBundle,
    skin: Skin,
    private val gameplayHelper: GameplayHelper
): Table() {
    private val countdownLabel = Label("3", skin, FONT_180_BORDERED, Color.WHITE)

    init {
        setFillParent(true)
        add(countdownLabel)
        gameplayHelper.addGameplayListener(GameplayEventListener())
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (isVisible) {
            val newTime = GameplayState.Init.START_TIME - gameplayHelper.getState().elapsedTime.toInt()
            val labelText = if (newTime > 0) {
                newTime.toString()
            } else {
                i18N.get("game_go")
            }

            countdownLabel.setText(labelText)
        }
    }

    private inner class GameplayEventListener: GameplayHelper.GameplayEventListener {
        override fun onGameplayStateChanged(state: GameplayState) {
            isVisible = state is GameplayState.Init
        }
    }
}
