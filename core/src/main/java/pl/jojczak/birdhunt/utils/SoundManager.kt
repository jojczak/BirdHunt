package pl.jojczak.birdhunt.utils

import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.utils.Preferences.PREF_SOUND

class SoundManager {
    private var soundEnabled = Preferences.get(PREF_SOUND)

    init {
        Preferences.addListener(PREF_SOUND) {
            soundEnabled = it
        }
    }

    enum class Sound {
        START_COUNTDOWN,
        GUN_SHOT,
        GUN_RELOAD,
        BIRD_FALLING,
        BIRD_FLYING,
        LVL_UP,
        GAME_OVER
    }

    private val soundsMap = mapOf<Sound, com.badlogic.gdx.audio.Sound>(
        Sound.START_COUNTDOWN to AssetsLoader.get(Asset.SN_START_COUNTDOWN),
        Sound.GUN_SHOT to AssetsLoader.get(Asset.SN_GUN_SHOT),
        Sound.GUN_RELOAD to AssetsLoader.get(Asset.SN_GUN_RELOAD),
        Sound.BIRD_FALLING to AssetsLoader.get(Asset.SN_BIRD_FALLING),
        Sound.BIRD_FLYING to AssetsLoader.get(Asset.SN_BIRD_FLYING),
        Sound.LVL_UP to AssetsLoader.get(Asset.SN_LVL_UP),
        Sound.GAME_OVER to AssetsLoader.get(Asset.SN_GAME_OVER),
    )

    fun play(sound: Sound) {
        if (soundEnabled) soundsMap[sound]?.play()
    }

    fun pause() {
        soundsMap.forEach {
            it.value.pause()
        }
    }

    fun resume() {
        soundsMap.forEach {
            it.value.resume()
        }
    }

    fun stop() {
        soundsMap.forEach {
            it.value.stop()
        }
    }
}
