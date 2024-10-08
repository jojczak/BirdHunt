package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_180_BORDERED
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class CountdownLabel(
    private val i18N: I18NBundle,
    skin: Skin,
): Table(), GameplayLogic.FromActions {
    private val countdownLabel = Label("${GameplayLogic.INIT_TIME}", skin, FONT_180_BORDERED, Color.WHITE)

    init {
        setFillParent(true)
        add(countdownLabel)
    }

    override fun startCountdownUpdate(time: Float) {
        val remainingTime = (GameplayLogic.INIT_TIME - time + 1).toInt()
        val remainingTimeString = if (remainingTime > 0) {
            remainingTime.toString()
        } else {
            i18N.get("game_go")
        }
        countdownLabel.setText(remainingTimeString)
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        isVisible = state is GameplayState.Init
    }
}
