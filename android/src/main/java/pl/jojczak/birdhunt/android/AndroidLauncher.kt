package pl.jojczak.birdhunt.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import pl.jojczak.birdhunt.main.Main
import pl.jojczak.birdhunt.utils.appVersion
import pl.jojczak.birdhunt.utils.insetsHelperInstance
import pl.jojczak.birdhunt.utils.sPenHelperInstance

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sPenHelperInstance = SPenHelperAndroidImpl(this)
        insetsHelperInstance = InsetsHelperAndroidImpl(window)
        appVersion = packageManager.getPackageInfo(packageName, 0).versionName

        setView()
    }

    private fun setView() = setContentView(
        initializeForView(
            Main(),
            AndroidApplicationConfiguration().apply {
                useImmersiveMode = false
            }
        )
    )

    override fun onResume() {
        super.onResume()
        sPenHelperInstance.connect({}, {})
    }

    override fun onPause() {
        super.onPause()
        sPenHelperInstance.disconnect()
    }
}
