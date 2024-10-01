package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.scenes.scene2d.Actor
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper.Companion.BIRDS_PER_ROUND
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper.Companion.BIRD_SPAWN_DELAY
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper.Companion.SHOTS
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper.Companion.SHOT_TIME
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper

interface GameplayHelper {
    fun getState(): GameplayState
    fun addGameplayListener(listener: GameplayEventListener)
    fun removeGameplayListener(listener: GameplayEventListener)
    fun action(action: GameplayAction)

    interface GameplayEventListener {
        fun onGameplayStateChanged(state: GameplayState) = Unit
        fun spawnBird(delay: Float) = Unit
        fun gunShot() = Unit
        fun birdHit() = Unit
        fun shotsUpdated(shots: Int) = Unit
        fun hitUpdated(hit: Int) = Unit
        fun roundUpdated(round: Int) = Unit
        fun reset() = Unit
    }

    sealed class GameplayAction {
        data object PauseGame : GameplayAction()
        data object ResumeGame : GameplayAction()
        data object ExitGame : GameplayAction()
        data object Shot : GameplayAction()
        data object ShotMissed : GameplayAction()
        data object BirdHit : GameplayAction()
        data object BirdSpawned : GameplayAction()
        data object RestartGame : GameplayAction()
    }

    companion object {
        const val BIRDS_PER_ROUND = 6
        const val BIRD_SPAWN_DELAY = 1f
        const val SHOT_TIME = 6f
        const val SHOTS = 3
    }
}

class ScreenGameplayHelper(
    private val gameplayScreenActionReceiver: (action: GameplayScreenAction) -> Unit
) : GameplayHelper, SPenHelper.EventListener, Actor() {
    private val gameplayListeners = mutableListOf<GameplayHelper.GameplayEventListener>()

    private var gameplayState: GameplayState = GameplayState.Init()
        set(value) {
            field = value
            gameplayListeners.notify { onGameplayStateChanged(value) }
        }

    private var shots = SHOTS
        set(value) {
            field = value
            gameplayListeners.notify { shotsUpdated(value) }
        }

    private var hit = 0
        set(value) {
            field = value
            gameplayListeners.notify { hitUpdated(value) }
        }

    private var round = 1
        set(value) {
            field = value
            gameplayListeners.notify { roundUpdated(value) }
        }

    private var canShot = true

    override fun act(delta: Float) {
        super.act(delta)
        gameplayState.elapsedTime += delta

        when (gameplayState) {
            is GameplayState.Init -> {
                if (gameplayState.elapsedTime > GameplayState.Init.START_TIME + 1) {
                    gameplayState = GameplayState.Playing()
                    gameplayListeners.notify { spawnBird(delay = 0f) }
                }
            }

            is GameplayState.Paused -> Unit
            is GameplayState.Playing -> {
                if (gameplayState.elapsedTime > SHOT_TIME) {
                    gameplayState = GameplayState.GameOver.OutOfTime
                }
            }
            is GameplayState.GameOver -> Unit
        }
    }

    override fun getState(): GameplayState = gameplayState

    override fun addGameplayListener(listener: GameplayHelper.GameplayEventListener) {
        gameplayListeners.add(listener)
    }

    override fun removeGameplayListener(listener: GameplayHelper.GameplayEventListener) {
        gameplayListeners.remove(listener)
    }

    override fun action(action: GameplayHelper.GameplayAction) {
        when (action) {
            GameplayHelper.GameplayAction.PauseGame -> {
                if (gameplayState is GameplayState.Paused) return
                gameplayState = GameplayState.Paused(gameplayState)
            }

            GameplayHelper.GameplayAction.ResumeGame -> {
                if (gameplayState is GameplayState.Paused) {
                    gameplayState = (gameplayState as GameplayState.Paused).previousState
                }
            }

            GameplayHelper.GameplayAction.ExitGame -> {
                gameplayScreenActionReceiver(GameplayScreenAction.Exit)
            }

            GameplayHelper.GameplayAction.Shot -> {
                if (gameplayState is GameplayState.Playing && canShot) {
                    canShot = false
                    shots--
                    gameplayListeners.notify { gunShot() }
                }
            }

            GameplayHelper.GameplayAction.ShotMissed -> {
                if (shots > 0) {
                    canShot = true
                } else {
                    gameplayState = GameplayState.GameOver.OutOfAmmo
                }
            }

            GameplayHelper.GameplayAction.BirdHit -> {
                gameplayListeners.notify { birdHit() }
                gameplayState.elapsedTime = 0f
                hit++

                if (hit < BIRDS_PER_ROUND) {
                    gameplayListeners.notify { spawnBird(delay = BIRD_SPAWN_DELAY) }
                } else {
                    hit = 0
                    round++
                    gameplayListeners.notify { spawnBird(delay = BIRD_SPAWN_DELAY * 2) }
                }
            }


            GameplayHelper.GameplayAction.BirdSpawned -> {
                canShot = true
                shots = SHOTS
            }

            GameplayHelper.GameplayAction.RestartGame -> {
                shots = SHOTS
                hit = 0
                round = 1
                gameplayState = GameplayState.Init()
                gameplayListeners.notify { reset() }
            }
        }
    }

    private fun MutableList<GameplayHelper.GameplayEventListener>.notify(
        action: GameplayHelper.GameplayEventListener.() -> Unit
    ) {
        for (listener in this.toList()) listener.action()
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) {
        when (event) {
            SPenHelper.ButtonEvent.DOWN -> action(GameplayHelper.GameplayAction.Shot)
            SPenHelper.ButtonEvent.UP -> Unit
            SPenHelper.ButtonEvent.UNKNOWN -> Unit
        }
    }

    override fun onSPenMotionEvent(x: Float, y: Float) = Unit
}
