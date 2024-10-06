package pl.jojczak.birdhunt.lwjgl3

import pl.jojczak.birdhunt.utils.InsetsHelper

class InsetsHelperDesktopImpl: InsetsHelper {
    override var lastInsets = InsetsHelper.WindowInsets(0, 0, 0, 0)

    override fun addOnInsetsChangedListener(listener: InsetsHelper.OnInsetsChangedListener) {
        listener.onInsetsChanged(lastInsets)
    }

    override fun removeOnInsetsChangedListener(listener: InsetsHelper.OnInsetsChangedListener) = Unit
}
