package pl.jojczak.birdhunt.screens.gameplay

sealed class GameplayState(
    var elapsedTime: Float = 0f
) {
    class Init : GameplayState() {
        companion object {
            const val START_TIME = 3
        }
    }

    class Playing : GameplayState()
    data class Paused(val previousState: GameplayState) : GameplayState()

    sealed class GameOver : GameplayState() {
        data object OutOfAmmo : GameOver()
        data object OutOfTime: GameOver()
    }
}
