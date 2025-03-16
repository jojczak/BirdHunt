package pl.jojczak.birdhunt.os.helpers

interface OsCoreHelper {
    fun showToast(message: String)
    fun setKeepScreenOn(value: Boolean)
}
