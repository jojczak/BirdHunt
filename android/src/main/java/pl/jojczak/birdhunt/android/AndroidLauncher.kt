package pl.jojczak.birdhunt.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import pl.jojczak.birdhunt.main.Main
import pl.jojczak.birdhunt.utils.spenhelper.sPenHelperInstance

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sPenHelperInstance = SPenHelperAndroidImpl(this)

        val configuration = AndroidApplicationConfiguration()
        configuration.useImmersiveMode = true // Recommended, but not required.
        initialize(Main(), configuration)
    }

    override fun onResume() {
        super.onResume()
        sPenHelperInstance.connect({}, {})
    }

    override fun onPause() {
        super.onPause()
        sPenHelperInstance.disconnect()
    }
}
