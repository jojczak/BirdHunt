package pl.jojczak.birdhunt.android

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.R
import pl.jojczak.birdhunt.os.helpers.OsCoreHelper

class OsCoreHelperAndroidImpl(
    private val context: Context,
    private val window: Window
): OsCoreHelper {
    override fun showToast(message: String) {
        Gdx.app.debug(TAG, "showToast: $message")
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.play_store_url)) // Dodanie tekstu
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name)) // Opcjonalny temat
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.app_name)))
    }

    companion object {
        private const val TAG = "OsCoreHelperAndroidImpl"
    }
}
