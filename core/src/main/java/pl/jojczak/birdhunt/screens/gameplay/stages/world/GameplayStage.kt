package pl.jojczak.birdhunt.screens.gameplay.stages.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.BulletActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.ScopeActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.utils.sPenHelperInstance

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
                gameplayHelper = gameplayHelper,
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
        gameplayHelper.action(GameplayHelper.GameplayAction.BirdSpawned)
    }

    fun checkIfShotHitBird() = birdActor?.let { bird ->
        scopeActor.x > bird.x && scopeActor.x < bird.x + bird.width &&
            scopeActor.y > bird.y && scopeActor.y < bird.y + bird.height &&
            !bird.isDead
    } ?: false

    private inner class GameplayEventListener : GameplayHelper.GameplayEventListener {
        override fun spawnBird(delay: Float) {
            addAction(SequenceAction(
                DelayAction(delay),
                RunnableAction().apply {
                    setRunnable {
                        this@GameplayStage.spawnBird()
                    }
                }
            ))
        }

        override fun gunShot() {
            this@GameplayStage.gunShot()
            if (checkIfShotHitBird()) gameplayHelper.action(GameplayHelper.GameplayAction.BirdHit)
            else gameplayHelper.action(GameplayHelper.GameplayAction.ShotMissed)
        }
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
