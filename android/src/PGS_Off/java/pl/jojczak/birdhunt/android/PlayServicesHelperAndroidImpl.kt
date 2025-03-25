package pl.jojczak.birdhunt.android

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.R
import pl.jojczak.birdhunt.os.helpers.PlayServicesHelper
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.utils.Preferences.PREF_PGS_AUTH

class PlayServicesHelperAndroidImpl(
    private val activity: Activity
): PlayServicesHelper {
    override fun initPlayServices() {
        Preferences.put(PREF_PGS_AUTH, false)
        Preferences.flush()
    }

    override fun showLeaderboard() {
        showToast(R.string.pgs_unavailable_error)
    }

    override fun submitScore(score: Int) = Unit

    override fun showAchievements() {
        showToast(R.string.pgs_unavailable_error)
    }

    override fun unlockAchievement(id: String) = Unit

    override fun signIn() {
        showToast(R.string.pgs_unavailable_error)
    }

    override fun getUserName(callback: (String?) -> Unit) {
        callback(null)
    }

    private fun showToast(@StringRes message: Int) {
        Gdx.app.postRunnable {
            activity.runOnUiThread {
                Toast.makeText(activity, activity.getText(message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
