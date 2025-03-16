package pl.jojczak.birdhunt.lwjgl3

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.os.helpers.OsCoreHelper

class OsCoreHelperDesktopImpl: OsCoreHelper {
    override fun showToast(message: String) {
        Gdx.app.log(TAG, message)
    }

    override fun setKeepScreenOn(value: Boolean) {
        Gdx.app.log(TAG, "setKeepScreenOn: $value")
    }

    companion object {
        private const val TAG = "OsCoreHelperDesktopImpl"
    }
}
