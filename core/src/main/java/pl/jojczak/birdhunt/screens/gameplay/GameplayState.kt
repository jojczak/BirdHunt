package pl.jojczak.birdhunt.screens.gameplay

sealed class GameplayState {
    var paused = false
    var elapsedTime = 0f

    class Init : GameplayState()
    class Playing : GameplayState()
    sealed class GameOver(
        val points: Int,
        val killedBirds: Int,
        val firedShots: Int,
    ) : GameplayState() {
        class OutOfAmmo(points: Int, killedBirds: Int, firedShots: Int) :
            GameOver(points, killedBirds, firedShots)

        class OutOfTime(points: Int, killedBirds: Int, firedShots: Int) :
            GameOver(points, killedBirds, firedShots)
    }
}
