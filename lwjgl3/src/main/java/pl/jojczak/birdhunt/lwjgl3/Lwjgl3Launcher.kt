package pl.jojczak.birdhunt.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import pl.jojczak.birdhunt.main.Main
import pl.jojczak.birdhunt.utils.insetsHelperInstance
import pl.jojczak.birdhunt.utils.sPenHelperInstance

/** Launches the desktop (LWJGL3) application.  */

object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        if (StartupHelper.startNewJvmIfRequired()) return  // This handles macOS support and helps on Windows.
        sPenHelperInstance = SPenHelperDesktopImpl()
        insetsHelperInstance = InsetsHelperDesktopImpl()
        Lwjgl3Application(
            Main( onLoadingFinished = {} ),
            defaultConfiguration
        )
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Bird Hunt")
            //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
            //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
            useVsync(true)
            //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
            //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
            setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)
            //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
            //// useful for testing performance, but can also be very stressful to some hardware.
            //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
            setWindowedMode(DEFAULT_WIDTH, DEFAULT_HEIGHT)
            //setWindowSizeLimits(DEFAULT_WIDTH, DEFAULT_HEIGHT, Int.MAX_VALUE, Int.MAX_VALUE)
            //// You can change these files; they are in lwjgl3/src/main/resources/ .
            setWindowIcon(
                "libgdx128.png",
                "libgdx64.png",
                "libgdx32.png",
                "libgdx16.png"
            )
        }

    private const val DEFAULT_WIDTH = 853
    private const val DEFAULT_HEIGHT = 480
}
