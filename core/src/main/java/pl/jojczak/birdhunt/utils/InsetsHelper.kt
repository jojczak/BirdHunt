package pl.jojczak.birdhunt.utils

interface InsetsHelper {
    var lastInsets: WindowInsets
    fun addOnInsetsChangedListener(listener: OnInsetsChangedListener)
    fun removeOnInsetsChangedListener(listener: OnInsetsChangedListener)

    fun interface OnInsetsChangedListener {
        fun onInsetsChanged(insets: WindowInsets)
    }

    data class WindowInsets (
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    )
}
