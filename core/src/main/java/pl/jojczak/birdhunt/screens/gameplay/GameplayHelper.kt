package pl.jojczak.birdhunt.screens.gameplay

import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper

interface GameplayHelper {
    fun getState(): GameplayState
    fun addGameplayListener(listener: GameplayEventListener)
    fun action(action: GameplayAction)

    interface GameplayEventListener {
        fun onGameplayStateChanged(state: GameplayState) = Unit
        fun spawnBird() = Unit
        fun gunShot() = Unit
    }

    sealed class GameplayAction {
        data object PauseGame : GameplayAction()
        data object ResumeGame : GameplayAction()
        data object ExitGame : GameplayAction()
    }
}

abstract class ScreenGameplayHelper : GameplayHelper, SPenHelper.EventListener {
    abstract fun act(delta: Float)
}

class ScreenGameplayHelperImpl(
    private val gameplayScreenActionReceiver: (action: GameplayScreenAction) -> Unit
) : ScreenGameplayHelper() {
    private val gameplayListeners = mutableListOf<GameplayHelper.GameplayEventListener>()

    private var gameplayState: GameplayState = GameplayState.Init()
        set(value) {
            field = value
            gameplayListeners.forEach { it.onGameplayStateChanged(value) }
        }

    override fun act(delta: Float) {
        gameplayState.elapsedTime += delta

        when (gameplayState) {
            is GameplayState.Init -> {
                if (gameplayState.elapsedTime > GameplayState.Init.START_TIME + 1) {
                    gameplayState = GameplayState.Playing()
                    gameplayListeners.forEach { it.spawnBird() }
                }
            }

            is GameplayState.Paused -> Unit
            is GameplayState.Playing -> Unit
        }
    }

    override fun getState(): GameplayState = gameplayState

    override fun addGameplayListener(listener: GameplayHelper.GameplayEventListener) {
        gameplayListeners.add(listener)
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
        }
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) {
        when (event) {
            SPenHelper.ButtonEvent.DOWN -> {
                if (gameplayState is GameplayState.Playing) {
                    gameplayListeners.forEach { it.gunShot() }
                }
            }

            SPenHelper.ButtonEvent.UP -> Unit
            SPenHelper.ButtonEvent.UNKNOWN -> Unit
        }
    }

    override fun onSPenMotionEvent(x: Float, y: Float) = Unit

    companion object {
        const val BIRDS_PER_ROUND = 6
    }
}
