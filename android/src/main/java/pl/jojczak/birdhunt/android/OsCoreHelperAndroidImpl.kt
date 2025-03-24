package pl.jojczak.birdhunt.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.badlogic.gdx.Gdx
import com.google.android.play.core.review.ReviewManagerFactory
import pl.jojczak.birdhunt.R
import pl.jojczak.birdhunt.os.helpers.OsCoreHelper
import java.io.File

class OsCoreHelperAndroidImpl(
    private val context: Context,
    private val window: Window
): OsCoreHelper {
    private val reviewManager = ReviewManagerFactory.create(context)

    override fun showToast(message: String) {
        Gdx.app.debug(TAG, "showToast: $message")
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun setKeepScreenOn(value: Boolean) {
        Gdx.app.log(TAG, "setKeepScreenOn: $value")
        Handler(Looper.getMainLooper()).post {
            if (value) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    override fun shareApp() {
        Gdx.app.log(TAG, "share")

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.play_store_url))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_title))
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_title)))
    }

    override fun shareAppWithScreenshot(screenshotFile: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", screenshotFile)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_title)))
    }

    override fun reviewApp() {
        Gdx.app.log(TAG, "Reviewing app")
        val reviewRequest = reviewManager.requestReviewFlow()
        reviewRequest.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Gdx.app.log(TAG, "Successfully requested review flow")
                reviewManager.launchReviewFlow(context as Activity, task.result)
            } else {
                Gdx.app.error(TAG, "Failed to request review flow", task.exception)
            }
        }
    }

    companion object {
        private const val TAG = "OsCoreHelperAndroidImpl"
    }
}
