package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.Preferences

const val PREF_NAME = "BirdHuntPrefs"

data class Preference<T>(
    val key: String,
    val defaultValue: T
) {
    fun getFloat(preferences: Preferences): Float {
        return preferences.getFloat(key, defaultValue as Float)
    }

    fun getBoolean(preferences: Preferences): Boolean {
        return preferences.getBoolean(key, defaultValue as Boolean)
    }

    fun putFloat(preferences: Preferences, value: Float) {
        preferences.putFloat(key, value)
    }

    fun putBoolean(preferences: Preferences, value: Boolean) {
        preferences.putBoolean(key, value)
    }
}

val PREF_SENSITIVITY = Preference("sensitivity", 20f)
val PREF_SOUND = Preference("sound", true)
