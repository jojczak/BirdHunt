package pl.jojczak.birdhunt.screens.gameplay

sealed class GameplayState {
    var paused = false
    var elapsedTime = 0f

    class Init : GameplayState()
    class Playing : GameplayState()
    sealed class GameOver : GameplayState() {
        class OutOfAmmo : GameOver()
        class OutOfTime : GameOver()
    }
}
