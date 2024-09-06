package pl.jojczak.birdhunt.screens.mainmenu.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader

class LogoActor: Actor() {
    private val texture = AssetsLoader.get(Asset.TX_LOGO) as Texture

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, 0f, 0f)
    }
}
