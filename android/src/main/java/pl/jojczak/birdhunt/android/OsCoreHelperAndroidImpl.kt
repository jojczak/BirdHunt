package pl.jojczak.birdhunt.android

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import pl.jojczak.birdhunt.utils.OsCoreHelper

class OsCoreHelperAndroidImpl(
    private val context: Context
): OsCoreHelper {

    override fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
