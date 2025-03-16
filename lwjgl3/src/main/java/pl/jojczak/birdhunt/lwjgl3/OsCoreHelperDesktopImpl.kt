package pl.jojczak.birdhunt.lwjgl3

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.utils.OsCoreHelper

class OsCoreHelperDesktopImpl: OsCoreHelper {
    override fun showToast(message: String) {
        Gdx.app.log(TAG, message)
    }

    companion object {
        private const val TAG = "OsCoreHelperDesktopImpl"
    }
}
