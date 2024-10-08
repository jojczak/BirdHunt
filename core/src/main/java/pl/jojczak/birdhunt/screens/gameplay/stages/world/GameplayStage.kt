package pl.jojczak.birdhunt.screens.gameplay.stages.world

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.BulletActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.ScopeActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.utils.sPenHelperInstance

class GameplayStage() : BaseStage() {
    private val uiBackground = Image(createUIBackground())
    private var birdActor: BirdActor? = null
    private val scopeActor = ScopeActor()
    private val shotgunActor = ShotgunActor()

    init {
        addActor(scopeActor)
        addActor(uiBackground)
        addActor(shotgunActor)
        sPenHelperInstance.registerSPenEvents()
    }

    override fun act(delta: Float) {
        shotgunActor.scopePosition = Vector2(scopeActor.x, scopeActor.y)
        super.act(delta)
    }

    private fun createUIBackground() = Texture(
        Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(0f, 0f, 0f, 0.6f)
            fill()
        }
    )

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        super.onResize(scrWidth, scrHeight)
        uiBackground.setSize(width, ShotgunActor.FRAME_HEIGHT + 2f)
    }

    override fun dispose() {
        sPenHelperInstance.unregisterSPenEvents()
        super.dispose()
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameplayStage"
    }
}
