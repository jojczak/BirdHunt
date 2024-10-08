package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import pl.jojczak.birdhunt.screens.gameplay.GameplayLogic.Companion.HITS_PER_ROUND
import pl.jojczak.birdhunt.utils.SPenHelper
import pl.jojczak.birdhunt.utils.sPenHelperInstance

interface GameplayLogic {
    fun addActionsListener(vararg listener: FromActions)
    fun removeActionsListener(vararg listener: FromActions)
    fun <R>onAction(action: ToActions<R>): R

    interface FromActions {
        fun startCountdownUpdate(time: Float) = Unit
        fun gameplayStateUpdate(state: GameplayState) = Unit
        fun pointsUpdated(points: Int) = Unit
        fun shotsUpdated(shots: Int) = Unit
        fun hitUpdated(hit: Int) = Unit
        fun roundUpdated(round: Int) = Unit
        fun displayWarning(state: GameplayState.GameOver?) = Unit
        fun spawnBird(howMany: Int) = Unit
        fun shot() = Unit
        fun pauseStateUpdated(paused: Boolean) = Unit
        fun restartGame() = Unit
    }

    sealed class ToActions<R> {
        data object Shot : ToActions<Unit>()
        data object BirdHit : ToActions<Unit>()
        data object BirdsStillFlying : ToActions<Unit>()
        data object AllBirdsDead: ToActions<Unit>()
        data object AllBirdsRemoved : ToActions<Unit>()
        data object GetState: ToActions<GameplayState>()
        data object GetRound: ToActions<Int>()
        data object PauseGame: ToActions<Unit>()
        data object ResumeGame: ToActions<Unit>()
        data object RestartGame: ToActions<Unit>()
        data object ExitGame: ToActions<Unit>()
    }

    companion object {
        const val INIT_TIME = 3f

        const val DEF_POINTS = 0
        const val DEF_SHOTS = 3
        const val DEF_HIT = 0
        const val DEF_ROUND = 1

        const val POINTS_PER_HIT = 5
        const val HITS_PER_ROUND = 6
        const val TIME_TO_SHOT = 7
    }
}

class GameplayLogicImpl(
    private val gameplayScreenActionReceiver: (action: GameplayScreenAction) -> Unit
) : GameplayLogic, SPenHelper.EventListener, Actor() {
    private val actionsListeners = mutableListOf<GameplayLogic.FromActions>()

    private var points = GameplayLogic.DEF_POINTS
        set(value) {
            field = value
            notifyActionsListeners { pointsUpdated(value) }
        }
    private var shots = GameplayLogic.DEF_SHOTS
        set(value) {
            field = value
            notifyActionsListeners { shotsUpdated(value) }
        }
    private var hit = GameplayLogic.DEF_HIT
        set(value) {
            field = value
            notifyActionsListeners { hitUpdated(value) }
        }
    private var round = GameplayLogic.DEF_ROUND
        set(value) {
            field = value
            notifyActionsListeners { roundUpdated(value) }
        }
    private var gameplayState: GameplayState = GameplayState.Init()
        set(value) {
            field = value
            notifyActionsListeners { gameplayStateUpdate(value) }
        }

    private var anyBirdsInAir = false

    init {
        sPenHelperInstance.registerSPenEvents()
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (gameplayState.paused) return
        when (gameplayState) {
            is GameplayState.Init -> actInit(gameplayState as GameplayState.Init, delta)
            is GameplayState.Playing -> actPlaying(gameplayState as GameplayState.Playing, delta)
            is GameplayState.GameOver -> actGameOver(gameplayState as GameplayState.GameOver, delta)
        }
    }

    private fun actInit(state: GameplayState.Init, delta: Float) {
        gameplayState.elapsedTime += delta
        notifyActionsListeners { startCountdownUpdate(state.elapsedTime) }
        if (state.elapsedTime - 1 >= GameplayLogic.INIT_TIME) {
            gameplayState = GameplayState.Playing()
            spawnBirdsAndResetShots()
        }
    }

    private fun actPlaying(state: GameplayState.Playing, delta: Float) {
        if (anyBirdsInAir) state.elapsedTime += delta
        if (state.elapsedTime >= GameplayLogic.TIME_TO_SHOT) {
            gameplayState = GameplayState.GameOver.OutOfTime()
        } else if (state.elapsedTime >= GameplayLogic.TIME_TO_SHOT - 1.5f) {
            notifyActionsListeners { displayWarning(GameplayState.GameOver.OutOfTime()) }
        }
    }

    private fun actGameOver(state: GameplayState.GameOver, delta: Float) = Unit

    override fun <R>onAction(action: GameplayLogic.ToActions<R>): R {
        when (action) {
            is GameplayLogic.ToActions.Shot -> {
                if (anyBirdsInAir && !gameplayState.paused && gameplayState is GameplayState.Playing) {
                    shots--
                    notifyActionsListeners { shot() }
                }
            }

            is GameplayLogic.ToActions.BirdHit -> {
                points += GameplayLogic.POINTS_PER_HIT * round
                hit++
            }

            is GameplayLogic.ToActions.BirdsStillFlying -> {
                if (shots == 0) {
                    notifyActionsListeners { displayWarning(GameplayState.GameOver.OutOfAmmo()) }
                    gameplayState = GameplayState.GameOver.OutOfAmmo()
                }
            }

            is GameplayLogic.ToActions.AllBirdsDead -> {
                notifyActionsListeners { displayWarning(null) }
                anyBirdsInAir = false
                gameplayState.elapsedTime = 0f
            }

            is GameplayLogic.ToActions.AllBirdsRemoved -> {
                if (gameplayState is GameplayState.Playing) {
                    if (hit >= HITS_PER_ROUND) {
                        round++
                        delayAction(2f) {
                            hit = GameplayLogic.DEF_HIT
                            spawnBirdsAndResetShots()
                        }
                    } else {
                        delayAction(1f) { spawnBirdsAndResetShots() }
                    }
                }
            }

            is GameplayLogic.ToActions.GetState -> {
                @Suppress("UNCHECKED_CAST")
                return gameplayState as R
            }

            is GameplayLogic.ToActions.GetRound -> {
                @Suppress("UNCHECKED_CAST")
                return round as R
            }

            is GameplayLogic.ToActions.PauseGame -> {
                notifyActionsListeners { pauseStateUpdated(true) }
                gameplayState.paused = true
            }

            is GameplayLogic.ToActions.ResumeGame -> {
                notifyActionsListeners { pauseStateUpdated(false) }
                gameplayState.paused = false
            }

            is GameplayLogic.ToActions.RestartGame -> {
                gameplayState = GameplayState.Init()
                points = GameplayLogic.DEF_POINTS
                shots = GameplayLogic.DEF_SHOTS
                hit = GameplayLogic.DEF_HIT
                round = GameplayLogic.DEF_ROUND
                notifyActionsListeners { restartGame() }
                notifyActionsListeners { displayWarning(null) }
            }

            is GameplayLogic.ToActions.ExitGame -> {
                gameplayScreenActionReceiver(GameplayScreenAction.NavigateToMainMenu)
            }
        }

        @Suppress("UNCHECKED_CAST")
        return Unit as R
    }

    private fun spawnBirdsAndResetShots() {
        shots = GameplayLogic.DEF_SHOTS
        when (round) {
            in 0..5 -> {
                notifyActionsListeners { spawnBird(1) }
            }

            in 6..10 -> {
                notifyActionsListeners { spawnBird(2) }
            }

            else -> {
                notifyActionsListeners { spawnBird(3) }
            }
        }
        anyBirdsInAir = true
    }

    private fun delayAction(delay: Float, action: () -> Unit) {
        addAction(SequenceAction(
            DelayAction(delay),
            RunnableAction().apply {
                setRunnable { action() }
            }
        ))
    }

    override fun onSPenButtonEvent(event: SPenHelper.ButtonEvent) = when (event) {
        SPenHelper.ButtonEvent.DOWN -> onAction(GameplayLogic.ToActions.Shot)
        SPenHelper.ButtonEvent.UP, SPenHelper.ButtonEvent.UNKNOWN -> Unit
    }

    override fun onSPenMotionEvent(x: Float, y: Float) = Unit

    private fun notifyActionsListeners(action: GameplayLogic.FromActions.() -> Unit) {
        actionsListeners.forEach { it.action() }
    }

    override fun addActionsListener(vararg listener: GameplayLogic.FromActions) {
        actionsListeners.addAll(listener)
    }

    override fun removeActionsListener(vararg listener: GameplayLogic.FromActions) {
        actionsListeners.removeAll(listener.toSet())
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "GameplayLogicImpl"
    }
}
