package pl.jojczak.birdhunt.stages.background.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor

open class BackgroundActor(
    textureAsset: Asset
) : BaseActor() {
    protected val texture = AssetsLoader.get<Texture>(textureAsset).also { tx ->
        setSize(tx.width.toFloat(), tx.height.toFloat())
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            texture,
            x,
            y,
            width / 2,
            height / 2,
            width,
            height,
            scaleX,
            scaleY,
            rotation,
            0,
            0,
            texture.width,
            texture.height,
            false,
            false
        )
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        val worldWidth = stage.viewport.worldWidth

        setScale(
            if (worldWidth > width) worldWidth / width
            else 1f
        )

        x = (worldWidth - width) / 2f
    }
}
