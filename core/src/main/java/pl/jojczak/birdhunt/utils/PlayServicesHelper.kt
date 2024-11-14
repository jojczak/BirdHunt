package pl.jojczak.birdhunt.utils

interface PlayServicesHelper {
    fun initPlayServices()
    fun showLeaderboard()
    fun submitScore(score: Int)
    fun showAchievements()
    fun unlockAchievement(id: String)
    fun signIn()

    companion object {
        const val SHOW_LEADERBOARD_REQUEST = 1001
        const val SHOW_ACHIEVEMENT_REQUEST = 1002

        const val LEADERBOARD_ID = "CgkI3rXIsPgBEAIQAA"
        const val ACHIEVEMENT_KILL_TWO_BIRDS = "CgkI3rXIsPgBEAIQAg"
        const val ACHIEVEMENT_1000_POINTS = "CgkI3rXIsPgBEAIQBA"
        const val ACHIEVEMENT_KILL_THREE_BIRDS = "CgkI3rXIsPgBEAIQAw"
    }
}
