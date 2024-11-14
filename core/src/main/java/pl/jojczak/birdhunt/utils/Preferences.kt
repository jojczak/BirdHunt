package pl.jojczak.birdhunt.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Preferences {
    private const val PREF_NAME = "BirdHuntPrefs"
    val gdxPreferences: Preferences = Gdx.app.getPreferences(PREF_NAME)

    val PREF_SENSITIVITY = Preference("sensitivity", 20f)
    val PREF_SOUND = Preference("sound", true)
    val PREF_HIGH_SCORE = Preference("highScore", 0)
    val PREF_GAME_SCALE = Preference("gameScale", 1f)
    val PREF_FIRST_RUN = Preference("firstRun", true)
    val PREF_FIRST_GAME = Preference("firstGame", true)
    val PREF_PGS_AUTH = Preference("pgsAuth", false)
    val PREF_ACH_TWO_BIRDS_UNLOCKED = Preference("achTwoBirdsUnlocked", false)
    val PREF_ACH_1K_POINTS_UNLOCKED = Preference("ach1KPointsUnlocked", false)
    val PREF_ACH_THREE_BIRDS_UNLOCKED = Preference("achThreeBirdsUnlocked", false)

    private val listeners = mutableMapOf<Preference<*>, MutableList<PreferenceListener<*>>>()

    inline fun <reified T: Any> get(preference: Preference<T>): T {
        return when (T::class) {
            Int::class -> gdxPreferences.getInteger(preference.key, preference.defaultValue as Int) as T
            Float::class -> gdxPreferences.getFloat(preference.key, preference.defaultValue as Float) as T
            Boolean::class -> gdxPreferences.getBoolean(preference.key, preference.defaultValue as Boolean) as T
            else -> throw IllegalArgumentException("Unsupported preference type")
        }
    }

    inline fun <reified T : Any> put(preference: Preference<T>, value: T) {
        Gdx.app.log(TAG, "Putting \"${preference.key}\" with value: $value")
        when (T::class) {
            Int::class -> gdxPreferences.putInteger(preference.key, value as Int)
            Float::class -> gdxPreferences.putFloat(preference.key, value as Float)
            Boolean::class -> gdxPreferences.putBoolean(preference.key, value as Boolean)
        }
        notifyListeners(preference, value)
    }

    fun flush() {
        gdxPreferences.flush()
    }

    fun <T : Any> addListener(pref: Preference<T>, listener: PreferenceListener<T>) {
        if (listeners[pref] == null) { listeners[pref] = mutableListOf() }
        listeners[pref]?.add(listener)
    }

    fun <T : Any> removeListener(pref: Preference<T>, listener: PreferenceListener<T>) {
        listeners[pref]?.remove(listener)
    }

    fun <T : Any> notifyListeners(preference: Preference<T>, value: T) {
        listeners[preference]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as? PreferenceListener<T>)?.onPreferenceChanged(value)
        }
    }

    fun interface PreferenceListener<T : Any> {
        fun onPreferenceChanged(value: T)
    }

    data class Preference<T>(
        val key: String,
        val defaultValue: T
    )

    const val TAG = "Preferences"
}
