package pl.jojczak.birdhunt.lwjgl3

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper

class SPenHelperDesktopImpl : SPenHelper {
    private var connected = false
    private var registered = false

    private var lastMouseX = 0
    private var lastMouseY = 0

    override var implType = SPenHelper.ImplType.DESKTOP
    override val eventListeners = mutableListOf<SPenHelper.EventListener>()

    override fun connect(onSuccess: () -> Unit, onError: (SPenHelper.ConnectionError) -> Unit) {
        Gdx.app.log(TAG, "connecting to SPen (desktop)")
        connected = true
        onSuccess()
    }

    override fun disconnect() {
        Gdx.app.log(TAG, "disconnecting from SPen (desktop)")
        connected = false
    }

    override fun registerSPenEvents() {
        Gdx.app.log(TAG, "registering SPen events (desktop)")
        registered = true
    }

    override fun unregisterSPenEvents() {
        Gdx.app.log(TAG, "unregistering SPen events (desktop)")
        registered = false
    }

    override fun addEventListener(listener: SPenHelper.EventListener) {
        Gdx.app.log(TAG, "adding SPen event listener (desktop) $listener")
        eventListeners.add(listener)
    }

    override fun removeEventListener(listener: SPenHelper.EventListener) {
        Gdx.app.log(TAG, "removing SPen event listener (desktop) $listener")
        eventListeners.remove(listener)
    }

    override fun act() {
        if (!connected || !registered) {
            return
        }

        val mouseX = Gdx.input.getX(0)
        val mouseY = Gdx.input.getY(0)

        val diffX = mouseX - lastMouseX
        val diffY = mouseY - lastMouseY

        if (diffX != 0 || diffY != 0) {
            eventListeners.forEach { listener ->
                listener.onSPenMotionEvent(
                    x = diffX.toFloat() * DIFF_MULTIPLIER,
                    y = -diffY.toFloat() * DIFF_MULTIPLIER
                )
            }
        }

        lastMouseX = mouseX
        lastMouseY = mouseY

        if (Gdx.input.justTouched()) {
            Gdx.app.log(TAG, "justTouched")
            eventListeners.forEach { listener ->
                listener.onSPenButtonEvent(SPenHelper.ButtonEvent.DOWN)
                listener.onSPenButtonEvent(SPenHelper.ButtonEvent.UP)
            }
        }
    }

    companion object {
        private const val TAG = "SPenHelperDesktopImpl"

        private const val DIFF_MULTIPLIER = 0.005f
    }
}
