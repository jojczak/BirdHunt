package pl.jojczak.birdhunt.android

import android.app.Activity
import android.widget.Toast
import com.badlogic.gdx.Gdx
import com.google.android.gms.games.AuthenticationResult
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import com.google.android.gms.tasks.Task
import pl.jojczak.birdhunt.R
import pl.jojczak.birdhunt.utils.PlayServicesHelper
import pl.jojczak.birdhunt.utils.PlayServicesHelper.Companion.LEADERBOARD_ID
import pl.jojczak.birdhunt.utils.PlayServicesHelper.Companion.SHOW_ACHIEVEMENT_REQUEST
import pl.jojczak.birdhunt.utils.PlayServicesHelper.Companion.SHOW_LEADERBOARD_REQUEST
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_PGS_AUTH

class PlayServicesHelperAndroidImpl(
    private val activity: Activity
): PlayServicesHelper {

    override fun initPlayServices() {
        Gdx.app.log(TAG, "Initializing Play Services")
        PlayGamesSdk.initialize(activity)

        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.isAuthenticated().addOnCompleteListener(::onAuthenticationResult)
    }

    override fun showLeaderboard() {
        Gdx.app.log(TAG, "Showing leaderboard")
        if (!checkIsAuthenticatedViaPreferences(true)) return

        PlayGames.getLeaderboardsClient(activity)
            .getLeaderboardIntent(LEADERBOARD_ID)
            .addOnSuccessListener { intent ->
                activity.startActivityForResult(intent, SHOW_LEADERBOARD_REQUEST)
            }
            .addOnFailureListener { exception ->
                Gdx.app.error(TAG, exception.message)
                checkIsAuthenticatedViaPlayServices()
            }
    }

    override fun submitScore(score: Int) {
        Gdx.app.log(TAG, "Submitting score: ${score.toLong()}")
        if (!checkIsAuthenticatedViaPreferences(false)) return

        PlayGames.getLeaderboardsClient(activity)
            .submitScore(LEADERBOARD_ID, score.toLong())
    }

    override fun showAchievements() {
        Gdx.app.log(TAG, "Showing achievements")
        if (!checkIsAuthenticatedViaPreferences(true)) return

        PlayGames.getAchievementsClient(activity)
            .achievementsIntent
            .addOnSuccessListener { intent ->
                activity.startActivityForResult(intent, SHOW_ACHIEVEMENT_REQUEST)
            }
            .addOnFailureListener { exception ->
                Gdx.app.error(TAG, exception.message)
                checkIsAuthenticatedViaPlayServices()
            }
    }

    override fun unlockAchievement(id: String) {
        Gdx.app.log(TAG, "Unlocking achievement: $id")
        PlayGames.getAchievementsClient(activity).unlock(id)
    }

    override fun signIn() {
        Gdx.app.log(TAG, "Signing in")
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn().addOnCompleteListener(::onAuthenticationResult)
    }

    private fun checkIsAuthenticatedViaPreferences(showError: Boolean): Boolean {
        return if (!Preferences.get(PREF_PGS_AUTH)) {
            Gdx.app.log(TAG, "Not authenticated")

            if (showError) {
                Gdx.app.postRunnable {
                    activity.runOnUiThread {
                        Toast.makeText(
                            activity,
                            R.string.pgs_unauthenticated_error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            false
        } else true
    }

    private fun checkIsAuthenticatedViaPlayServices() {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.isAuthenticated().addOnCompleteListener(::onAuthenticationResult)
    }

    private fun onAuthenticationResult(isAuthenticatedTask: Task<AuthenticationResult>) {
        val isAuthenticated = isAuthenticatedTask.isSuccessful && isAuthenticatedTask.result.isAuthenticated
        Gdx.app.log(TAG, "Is authenticated: $isAuthenticated")

        Preferences.put(PREF_PGS_AUTH, isAuthenticated)
        Preferences.flush()
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "PlayServicesHelperAndroidImpl"
    }
}

