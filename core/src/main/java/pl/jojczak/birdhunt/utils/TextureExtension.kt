package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

fun Texture.getScaledTexture(scale: Float): Texture {
    if (!textureData.isPrepared) {
        textureData.prepare()
    }

    val originalPixmap = textureData.consumePixmap()

    val scaledWidth = (originalPixmap.width * scale).toInt()
    val scaledHeight = (originalPixmap.height * scale).toInt()

    val newTexture: Texture

    Pixmap(scaledWidth, scaledHeight, originalPixmap.format).apply {
        setFilter(Pixmap.Filter.NearestNeighbour)
        drawPixmap(originalPixmap, 0, 0, originalPixmap.width, originalPixmap.height, 0, 0, scaledWidth, scaledHeight)
        newTexture = Texture(this)
        dispose()
    }

    originalPixmap.dispose()

    return newTexture
}

fun TextureRegion.getScaledTexture(scale: Float): Texture {
    val textureData = texture.textureData

    if (!textureData.isPrepared) {
        textureData.prepare()
    }

    val originalPixmap = textureData.consumePixmap()

    val regionPixmap = Pixmap(regionWidth, regionHeight, originalPixmap.format).apply {
        drawPixmap(originalPixmap, 0, 0, regionX, regionY, regionWidth, regionHeight)
    }
    originalPixmap.dispose()

    val newWidth = (regionPixmap.width * scale).toInt()
    val newHeight = (regionPixmap.height * scale).toInt()

    val newTexture: Texture

    Pixmap(newWidth, newHeight, originalPixmap.format).apply {
        setFilter(Pixmap.Filter.NearestNeighbour)
        drawPixmap(regionPixmap, 0, 0, regionPixmap.width, regionPixmap.height, 0, 0, newWidth, newHeight)
        newTexture = Texture(this)
        dispose()
    }

    regionPixmap.dispose()

    return newTexture
}
