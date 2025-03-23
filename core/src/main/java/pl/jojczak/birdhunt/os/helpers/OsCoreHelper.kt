package pl.jojczak.birdhunt.os.helpers

import java.io.File

interface OsCoreHelper {
    fun showToast(message: String)
    fun setKeepScreenOn(value: Boolean)
    fun shareApp()
    fun shareAppWithScreenshot(screenshotFile: File)
}
