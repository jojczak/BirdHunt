package pl.jojczak.birdhunt.lwjgl3

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.os.helpers.OsCoreHelper
import java.io.File

class OsCoreHelperDesktopImpl: OsCoreHelper {
    override fun showToast(message: String) {
        Gdx.app.log(TAG, message)
    }

    override fun setKeepScreenOn(value: Boolean) {
        Gdx.app.log(TAG, "setKeepScreenOn: $value")
    }

    override fun shareApp() {
        Gdx.app.log(TAG, "share")
    }

    override fun shareAppWithScreenshot(screenshotFile: File) {
        Gdx.app.log(TAG, "shareWithScreenshot: $screenshotFile")
    }

    override fun reviewApp() {
        Gdx.app.log(TAG, "Reviewing app")
    }

    companion object {
        private const val TAG = "OsCoreHelperDesktopImpl"
    }
}
