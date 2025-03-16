package pl.jojczak.birdhunt.android

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import pl.jojczak.birdhunt.main.Main
import pl.jojczak.birdhunt.os.helpers.osCoreHelper
import pl.jojczak.birdhunt.os.helpers.appVersion
import pl.jojczak.birdhunt.os.helpers.insetsHelperInstance
import pl.jojczak.birdhunt.os.helpers.playServicesHelperInstance
import pl.jojczak.birdhunt.os.helpers.sPenHelperInstance

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {

    private lateinit var splashScreenHelper: SplashScreenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreenHelper = SplashScreenHelper(this, installSplashScreen())
        sPenHelperInstance = SPenHelperAndroidImpl(this)
        insetsHelperInstance = InsetsHelperAndroidImpl(window)
        playServicesHelperInstance = PlayServicesHelperAndroidImpl(this)
        osCoreHelper = OsCoreHelperAndroidImpl(this, window)
        appVersion = packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0"

        setView()

        playServicesHelperInstance.initPlayServices()
    }

    private fun setView() = setContentView(
        initializeForView(
            Main(splashScreenHelper::startExitAnimation),
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
