package pl.jojczak.birdhunt.stages.gameplay

import com.badlogic.gdx.math.Vector2
import pl.jojczak.birdhunt.actors.BulletActor
import pl.jojczak.birdhunt.actors.ScopeActor
import pl.jojczak.birdhunt.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

class GameplayStage(
    private val gameplayHelper: GameplayHelper
) : BaseStage() {
    private var birdActor: BirdActor? = null
    private val scopeActor = ScopeActor(gameplayHelper)
    private val shotgunActor = ShotgunActor()

    init {
        addActor(scopeActor)
        addActor(shotgunActor)
        gameplayHelper.addGameplayListener(GameplayEventListener())
        sPenHelperInstance.registerSPenEvents()
    }

    override fun act(delta: Float) {
        shotgunActor.scopePosition = Vector2(scopeActor.x, scopeActor.y)
        super.act(delta)
    }

    private fun gunShot() {
        addActor(
            BulletActor(
                startPos = Vector2(shotgunActor.getBarrelPos()),
                endPos = Vector2(scopeActor.x, scopeActor.y),
                angle = shotgunActor.angleToScope.degrees
            )
        )
    }

    private fun spawnBird() {
        birdActor = BirdActor(gameplayHelper)
        addActor(birdActor)
        birdActor?.toBack()
    }

    private inner class GameplayEventListener: GameplayHelper.GameplayEventListener {
        override fun spawnBird() {
            this@GameplayStage.spawnBird()
        }

        override fun gunShot() {
            this@GameplayStage.gunShot()
        }
    }

    override fun dispose() {
        sPenHelperInstance.unregisterSPenEvents()
        super.dispose()
    }

    companion object {
        private const val TAG = "GameplayStage"
    }
}
