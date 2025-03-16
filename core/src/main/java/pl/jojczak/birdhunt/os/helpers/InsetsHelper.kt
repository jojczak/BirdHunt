package pl.jojczak.birdhunt.os.helpers

interface InsetsHelper {
    var lastInsets: WindowInsets
    fun addOnInsetsChangedListener(listener: OnInsetsChangedListener)
    fun removeOnInsetsChangedListener(listener: OnInsetsChangedListener)

    fun interface OnInsetsChangedListener {
        fun onInsetsChanged(insets: WindowInsets)
    }

    data class WindowInsets (
        val left: Int = 0,
        val top: Int = 0,
        val right: Int = 0,
        val bottom: Int = 0
    )

    data class WindowInsetsF (
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 0f,
        val bottom: Float = 0f
    )
}
