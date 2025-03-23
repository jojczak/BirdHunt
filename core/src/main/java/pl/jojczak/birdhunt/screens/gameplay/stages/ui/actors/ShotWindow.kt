package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.utils.getScaledTexture

class ShotWindow(
    i18N: I18NBundle,
    skin: Skin
) : BottomUIWindow(
    i18N.get("game_label_shot"),
    skin,
    "dark"
), GameplayLogic.FromActions {

    private val bulletImageNormal =
        AssetsLoader.get<Texture>(Asset.UI_BULLET).getScaledTexture(NORMAL_BULLET_SCALE)
    private val bulletImageSmall =
        AssetsLoader.get<Texture>(Asset.UI_BULLET).getScaledTexture(SMALL_BULLET_SCALE)

    init {
        shotsUpdated(GameplayLogic.DEF_SHOTS)
    }

    override fun shotsUpdated(shots: Int) {
        val bulletTx = if (shots > 3)
            bulletImageSmall
        else
            bulletImageNormal

        clear()
        for (i in 0 until shots) {
            add(Image(bulletTx)).apply {
                if (i > 0) padLeft(PAD)
            }
        }
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        if (state is GameplayState.GameOver.OutOfAmmo) addAction(getFlashingAnim(this))
    }

    override fun restartGame() {
        removeFlashingAnim()
        isVisible = true
    }

    companion object {
        private const val PAD = 10f

        private const val NORMAL_BULLET_SCALE = 5f
        private const val SMALL_BULLET_SCALE = 4f
    }
}
