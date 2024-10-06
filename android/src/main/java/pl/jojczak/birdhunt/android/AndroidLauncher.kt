package pl.jojczak.birdhunt.android

import android.content.res.Configuration
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import pl.jojczak.birdhunt.main.Main
import pl.jojczak.birdhunt.utils.insetsHelperInstance
import pl.jojczak.birdhunt.utils.sPenHelperInstance

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sPenHelperInstance = SPenHelperAndroidImpl(this)
        insetsHelperInstance = InsetsHelperAndroidImpl(window)

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

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
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
