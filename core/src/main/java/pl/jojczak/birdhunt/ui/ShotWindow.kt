package pl.jojczak.birdhunt.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader

class ShotWindow(
    i18N: I18NBundle,
    skin: Skin
): Window(
    i18N.get("game_label_shot"),
    skin,
    "dark"
) {
    var shots = 1
        set(value) {
            field = value
            updateShots()
        }

    init {
        isMovable = false
        isResizable = false
    }

    private fun updateShots() {
        clear()
        for (i in 0 until shots) {
            add(getBulletImage()).apply {
                if (i > 0) padLeft(PAD)
            }
        }
    }

    private fun getBulletImage() = Image(AssetsLoader.get<Texture>(Asset.UI_BULLET))

    companion object {
        private const val PAD = 10f
    }
}
