package pl.jojczak.birdhunt.android

import android.content.Context
import com.badlogic.gdx.Gdx
import com.samsung.android.sdk.penremote.AirMotionEvent
import com.samsung.android.sdk.penremote.ButtonEvent
import com.samsung.android.sdk.penremote.SpenEvent
import com.samsung.android.sdk.penremote.SpenEventListener
import com.samsung.android.sdk.penremote.SpenRemote
import com.samsung.android.sdk.penremote.SpenUnit
import com.samsung.android.sdk.penremote.SpenUnitManager
import pl.jojczak.birdhunt.utils.spenhelper.SPenHelper

class SPenHelperAndroidImpl(
    private val context: Context
) : SPenHelper {
    private var sPenUnitManager: SpenUnitManager? = null

    override var implType = SPenHelper.ImplType.ANDROID
    override val eventListeners = mutableListOf<SPenHelper.EventListener>()

    override fun connect(
        onSuccess: () -> Unit,
        onError: (SPenHelper.ConnectionError) -> Unit
    ) {
        Gdx.app.log(TAG, "connecting to SPen...")
        val sPenRemote = SpenRemote.getInstance()

        if (!sPenRemote.isConnected) {
            sPenRemote.connect(context, ConnectionResultCallback(onSuccess, onError))
        } else {
            Gdx.app.error(TAG, "SPen already connected")
        }
    }

    private inner class ConnectionResultCallback(
        private val onSuccess: () -> Unit,
        private val onError: (SPenHelper.ConnectionError) -> Unit
    ) : SpenRemote.ConnectionResultCallback {
        override fun onSuccess(manager: SpenUnitManager) {
            Gdx.app.log(TAG, "SPen successfully connected")
            sPenUnitManager = manager
            onSuccess()
        }

        override fun onFailure(code: Int) {
            val error = SPenHelper.ConnectionError.codeToError(code)
            Gdx.app.error(TAG, "SPen connection failed: $error")
            onError(error)
        }
    }

    override fun disconnect() {
        Gdx.app.log(TAG, "disconnecting from SPen...")
        val sPenRemote = SpenRemote.getInstance()
        sPenRemote.disconnect(context)
    }

    override fun registerSPenEvents(): Unit = sPenUnitManager?.let { manager ->
        val buttonUnit = manager.getUnit(SpenUnit.TYPE_BUTTON)
        val motionUnit = manager.getUnit(SpenUnit.TYPE_AIR_MOTION)

        manager.registerSpenEventListener(SPenButtonListener(), buttonUnit)
        Gdx.app.log(TAG, "SPen button events registered")

        manager.registerSpenEventListener(SPenMotionListener(), motionUnit)
        Gdx.app.log(TAG, "SPen air motion events registered")
    } ?: run {
        Gdx.app.error(TAG, "SPen not connected")
    }

    private inner class SPenButtonListener : SpenEventListener {
        override fun onEvent(event: SpenEvent) {
            val buttonEvent = ButtonEvent(event).toLocalEvent()
            Gdx.app.log(TAG, "SPen button event received: $buttonEvent")
            eventListeners.forEach { listener ->
                listener.onSPenButtonEvent(
                    event = buttonEvent
                )
            }
        }
    }

    private inner class SPenMotionListener : SpenEventListener {
        override fun onEvent(event: SpenEvent) {
            val airMotionEvent = AirMotionEvent(event)

            eventListeners.forEach { listener ->
                listener.onSPenMotionEvent(
                    x = airMotionEvent.deltaX,
                    y = airMotionEvent.deltaY
                )
            }
        }
    }

    override fun unregisterSPenEvents(): Unit = sPenUnitManager?.let { manager ->
        val buttonUnit = manager.getUnit(SpenUnit.TYPE_BUTTON)
        val motionUnit = manager.getUnit(SpenUnit.TYPE_AIR_MOTION)

        manager.unregisterSpenEventListener(buttonUnit)
        Gdx.app.log(TAG, "SPen button events unregistered")

        manager.unregisterSpenEventListener(motionUnit)
        Gdx.app.log(TAG, "SPen air motion events unregistered")
    } ?: run {
        Gdx.app.error(TAG, "SPen not connected")
    }

    override fun addEventListener(listener: SPenHelper.EventListener) {
        Gdx.app.log(TAG, "SPen event listener added: $listener")
        eventListeners.add(listener)
    }

    override fun removeEventListener(listener: SPenHelper.EventListener) {
        Gdx.app.log(TAG, "SPen event listener removed: $listener")
        eventListeners.remove(listener)
    }

    private fun ButtonEvent.toLocalEvent() = when (this.action) {
        ButtonEvent.ACTION_UP -> SPenHelper.ButtonEvent.UP
        ButtonEvent.ACTION_DOWN -> SPenHelper.ButtonEvent.DOWN
        else -> SPenHelper.ButtonEvent.UNKNOWN
    }

    override fun act() = Unit

    companion object {
        private const val TAG = "SPenHelperAndroidImpl"
    }
}
