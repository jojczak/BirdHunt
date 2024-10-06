package pl.jojczak.birdhunt.android

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import pl.jojczak.birdhunt.utils.InsetsHelper

class InsetsHelperAndroidImpl(window: Window) : InsetsHelper {
    override var lastInsets = InsetsHelper.WindowInsets(0, 0, 0, 0)
    private val insetsListener = mutableListOf<InsetsHelper.OnInsetsChangedListener>()

    init {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.navigationBars())
        }

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val displayCutoutInsets = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            notifyListeners(
                left = maxOf(displayCutoutInsets.left, systemBarsInsets.left),
                top = maxOf(displayCutoutInsets.top, systemBarsInsets.top),
                right = maxOf(displayCutoutInsets.right, systemBarsInsets.right),
                bottom = maxOf(displayCutoutInsets.bottom, systemBarsInsets.bottom)
            )
            insets
        }
    }

    private fun notifyListeners(left: Int, top: Int, right: Int, bottom: Int) {
        lastInsets = InsetsHelper.WindowInsets(left, top, right, bottom)
        insetsListener.forEach { listener -> listener.onInsetsChanged(lastInsets) }
    }

    override fun addOnInsetsChangedListener(listener: InsetsHelper.OnInsetsChangedListener) {
        insetsListener.add(listener)
        listener.onInsetsChanged(lastInsets)
    }

    override fun removeOnInsetsChangedListener(listener: InsetsHelper.OnInsetsChangedListener) {
        insetsListener.remove(listener)
    }
}
