package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_75
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper.Companion.BIRDS_PER_ROUND

class HitWindow(
    i18N: I18NBundle,
    skin: Skin,
    gameplayHelper: GameplayHelper
) : BottomUIWindow(
    i18N.get("game_label_hit"),
    skin,
    "dark"
) {
    private var hit = 0
        set(value) {
            field = value
            hitLabel.setText("$hit$MAX_BIRDS")
        }

    private val hitLabel = Label("0$MAX_BIRDS", skin, FONT_75, Color.WHITE)

    init {
        gameplayHelper.addGameplayListener(GameplayEventListener())
        add(hitLabel)
    }

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun hitUpdated(hit: Int) {
            this@HitWindow.hit = hit
        }

        override fun roundUpdated(round: Int) {
            hitLabel.setText("$BIRDS_PER_ROUND$MAX_BIRDS")
            addAction(
                getFlashingAnim(hitLabel) {
                    hitLabel.setText("$hit$MAX_BIRDS")
                }
            )
        }

        override fun reset() {
            for (action in actions) {
                removeAction(action)
                hit = hit
            }
        }
    }

    companion object {
        private const val MAX_BIRDS = "/${BIRDS_PER_ROUND}"
    }
}
