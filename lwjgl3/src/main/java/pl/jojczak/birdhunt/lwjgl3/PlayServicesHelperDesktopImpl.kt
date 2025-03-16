package pl.jojczak.birdhunt.lwjgl3

import pl.jojczak.birdhunt.os.helpers.PlayServicesHelper
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_PGS_AUTH

class PlayServicesHelperDesktopImpl: PlayServicesHelper {
    override fun initPlayServices() {
        Preferences.put(PREF_PGS_AUTH, false)
        Preferences.flush()
    }

    override fun showLeaderboard() = Unit
    override fun submitScore(score: Int) = Unit
    override fun showAchievements() = Unit
    override fun unlockAchievement(id: String) = Unit
    override fun signIn() = Unit
}
