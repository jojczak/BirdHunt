package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper

class ShotWindow(
    i18N: I18NBundle,
    skin: Skin,
    gameplayHelper: GameplayHelper
): BottomUIWindow(
    i18N.get("game_label_shot"),
    skin,
    "dark"
) {
    init {
        gameplayHelper.addGameplayListener(GameplayEventListener())
        updateShots(GameplayHelper.SHOTS)
    }

    private fun updateShots(shots: Int) {
        clear()
        for (i in 0 until shots) {
            add(getBulletImage()).apply {
                if (i > 0) padLeft(PAD)
            }
        }
    }

    private fun getBulletImage() = Image(AssetsLoader.get<Texture>(Asset.UI_BULLET))

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun shotsUpdated(shots: Int) {
            updateShots(shots)
        }
    }

    companion object {
        private const val PAD = 10f
    }
}
