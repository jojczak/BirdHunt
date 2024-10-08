package pl.jojczak.birdhunt.stages.background.actors

import pl.jojczak.birdhunt.assetsloader.Asset

class MovableBackgroundActor(
    textureAsset: Asset,
    private val screenBottom: Float,
    private val downScaleMultiplier: Float
) : BackgroundActor(
    textureAsset
) {
    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)

        y = (screenBottom - (scrWidth.toFloat() / scrHeight) * downScaleMultiplier)
            .coerceAtMost(stage.height - height + HEIGHT_CORRECTION)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "MovableBackgroundActor"

        private const val HEIGHT_CORRECTION = 10f
    }
}
