package pl.jojczak.birdhunt.utils.spenhelper

interface SPenHelper {
    val implType: ImplType
    val eventListeners: MutableList<EventListener>

    fun connect(onSuccess: () -> Unit, onError: (ConnectionError) -> Unit)
    fun disconnect()
    fun registerSPenEvents()
    fun unregisterSPenEvents()
    fun addEventListener(listener: EventListener)
    fun removeEventListener(listener: EventListener)

    //Only for desktop
    fun act()

    interface EventListener {
        fun onSPenButtonEvent(event: ButtonEvent)
        fun onSPenMotionEvent(x: Float, y: Float)
    }

    enum class ButtonEvent {
        DOWN,
        UP,
        UNKNOWN;
    }

    enum class ConnectionError(val code: Int) {
        UNSUPPORTED_DEVICE(-1),
        CONNECTION_FAILED(-2),
        UNKNOWN(-100);

        companion object {
            fun codeToError(code: Int) = ConnectionError.entries.find {
                it.code == code
            } ?: UNKNOWN
        }
    }

    enum class ImplType {
        ANDROID,
        DESKTOP
    }
}
