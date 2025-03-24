package pl.jojczak.birdhunt.screens.gameplay.stages.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic
import pl.jojczak.birdhunt.screens.gameplay.GameplayState
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.BulletActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.ScopeActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.birdactor.BirdActor
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.os.helpers.insetsHelperInstance
import pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.GameOverBird
import pl.jojczak.birdhunt.utils.realToStage

class GameplayStage(
    private val gameplayLogic: GameplayLogic
) : BaseStage(), GameplayLogic.FromActions {
    private val uiBackground = Image(createUIBackground())
    private val scopeActor = ScopeActor(gameplayLogic)
    private val shotgunActor = ShotgunActor()

    private val feathersParticle = Array(3) {
        ParticleEffect(AssetsLoader.get(Asset.PT_FEATHERS))
    }

    var bottomUISize = 0f
        set(value) {
            field = value.realToStage(this) + insetsHelperInstance.lastInsets.bottom.realToStage(this)
            onResize(Gdx.graphics.width, Gdx.graphics.height)
        }

    init {
        addActor(scopeActor)
        addActor(uiBackground)
        addActor(shotgunActor)
    }

    override fun act(delta: Float) {
        if (gameplayLogic.onAction(GameplayLogic.ToActions.GetState).paused) {
            root.actions.forEach { it.act(delta) }
            return
        }
        for (particle in feathersParticle) particle.takeIf { !it.isComplete }?.update(delta)
        shotgunActor.scopePosition = Vector2(scopeActor.x, scopeActor.y)
        super.act(delta)
    }

    override fun draw() {
        batch.begin()
        for (particle in feathersParticle) particle.takeIf { !it.isComplete }?.draw(batch)
        batch.end()
        super.draw()
    }

    private fun createUIBackground() = Texture(
        Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(0f, 0f, 0f, 0.6f)
            fill()
        }
    )

    override fun spawnBird(howMany: Int) = repeat(howMany) {
        BirdActor(gameplayLogic).also { bird ->
            bird.bottomUISize = bottomUISize
            addActor(bird)
            bird.toBack()
        }
    }

    override fun shot() {
        addActor(
            BulletActor(
                startPos = shotgunActor.getBarrelPos(),
                endPos = Vector2(scopeActor.x, scopeActor.y),
                angle = shotgunActor.angleToScope.degrees
            )
        )

        shotgunActor.startShotAnimation()

        var killedBirds = 0
        actors.filterIsInstance<BirdActor>().forEach { bird ->
            if (bird.isDead) return@forEach

            if (scopeActor.x > bird.x && scopeActor.x < bird.x + bird.width &&
                scopeActor.y > bird.y && scopeActor.y < bird.y + bird.height
            ) {
                if (bird.onHit()) {
                    killedBirds++
                    startFeatherParticle(bird)
                    gameplayLogic.onAction(GameplayLogic.ToActions.BirdHit)
                }
            }
        }
        gameplayLogic.onAction(GameplayLogic.ToActions.CheckBirdsKilledAchievements(killedBirds))

        if (actors.filterIsInstance<BirdActor>().all { it.isDead }) {
            gameplayLogic.onAction(GameplayLogic.ToActions.AllBirdsDead)
        } else {
            gameplayLogic.onAction(GameplayLogic.ToActions.BirdsStillFlying)
        }
    }

    override fun gameplayStateUpdate(state: GameplayState) {
        if (state is GameplayState.GameOver) {
            actors.filterIsInstance<BirdActor>().forEach { bird ->
                bird.onGameOver()
            }
            scopeActor.isVisible = false
            addActor(GameOverBird(1))
            addActor(GameOverBird(-1))
        }
    }

    override fun restartGame() {
        actors.filterIsInstance<BirdActor>().forEach { bird -> bird.remove() }
        actors.filterIsInstance<GameOverBird>().forEach { bird -> bird.remove() }
        scopeActor.apply {
            centerOnScreen()
            isVisible = true
        }
    }

    override fun actorRemoved(actor: Actor) {
        super.actorRemoved(actor)

        if (actors.filterIsInstance<BirdActor>().isEmpty()) {
            gameplayLogic.onAction(GameplayLogic.ToActions.AllBirdsRemoved)
        }
    }

    override fun onResize(scrWidth: Int, scrHeight: Int) {
        Gdx.app.log(TAG, "onResize, viewport size: ${viewport.worldWidth}, ${viewport.worldHeight}")
        super.onResize(scrWidth, scrHeight)

        uiBackground.setSize(width, bottomUISize)
        scopeActor.bottomUISize = bottomUISize
        actors.filterIsInstance<BirdActor>().forEach { it.bottomUISize = bottomUISize }
    }

    private fun startFeatherParticle(bird: BirdActor) {
        feathersParticle.firstOrNull { it.isComplete }?.let {
            it.reset()
            it.setPosition(bird.x + bird.width / 2, bird.y + bird.height / 2)
            it.emitters.first().apply {
                minParticleCount = (3..6).random()
                maxParticleCount = minParticleCount
            }
            it.start()
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameplayStage"
    }
}
