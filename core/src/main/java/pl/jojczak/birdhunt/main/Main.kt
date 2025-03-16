package pl.jojczak.birdhunt.main

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.ScreenWithUIStage
import pl.jojczak.birdhunt.screens.about.stages.AboutStage
import pl.jojczak.birdhunt.screens.gameplay.GameplayScreen
import pl.jojczak.birdhunt.screens.gameplay.stages.howtoplay.ControlsStage
import pl.jojczak.birdhunt.screens.loading.LoadingScreen
import pl.jojczak.birdhunt.screens.mainmenu.stages.MainMenuStage
import pl.jojczak.birdhunt.screens.settings.stages.SettingsStage
import pl.jojczak.birdhunt.screens.unsupporteddevice.stages.UnsupportedDeviceStage
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.Preferences
import pl.jojczak.birdhunt.os.helpers.SPenHelper
import pl.jojczak.birdhunt.utils.SoundManager
import pl.jojczak.birdhunt.os.helpers.sPenHelperInstance

class Main(
    private val onLoadingFinished: () -> Unit
) : Game() {
    private lateinit var soundManager: SoundManager
    private var backgroundStage: BackgroundStage? = null
    private var firstFrameDrawn = false

    override fun create() {
        Gdx.input.setCatchKey(Keys.BACK, true)

        if (sPenHelperInstance.implType == SPenHelper.ImplType.DESKTOP) {
            sPenHelperInstance.connect({}, {})
        }

        setScreen(LoadingScreen(::onAction))
    }

    private fun onAction(action: MainAction) {
        Gdx.app.log(TAG, "Action received: $action")

        when (action) {
            is MainAction.LoadingFinished -> {
                backgroundStage = action.bgStage
                soundManager = action.soundManager

                backgroundStage?.apply {
                    root.color.a = 1f
                    onResize(Gdx.graphics.width, Gdx.graphics.height)
                }

                if (sPenHelperInstance.isDeviceSupported()) {
                    if (Preferences.get(Preferences.PREF_FIRST_RUN)) {
                        Preferences.put(Preferences.PREF_FIRST_RUN, false)
                        Preferences.flush()
                        onAction(MainAction.NavigateToAbout)
                    } else {
                        onAction(MainAction.NavigateToMainMenu)
                    }
                } else {
                    onAction(MainAction.NavigateToUnsupportedDevice)
                }
            }

            MainAction.FirstFrameDrawn -> {
                firstFrameDrawn = true
                onLoadingFinished()
            }

            MainAction.NavigateToMainMenu -> {
                getScreen().dispose()
                setScreen(
                    ScreenWithUIStage(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        stage = MainMenuStage()
                    )
                )
            }

            MainAction.NavigateToUnsupportedDevice -> {
                getScreen().dispose()
                setScreen(
                    ScreenWithUIStage(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        stage = UnsupportedDeviceStage()
                    )
                )
            }

            MainAction.NavigateToGameplay -> {
                getScreen().dispose()
                setScreen(
                    GameplayScreen(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        soundManager = soundManager
                    )
                )
            }

            MainAction.NavigateToSettings -> {
                getScreen().dispose()
                setScreen(
                    ScreenWithUIStage(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        stage = SettingsStage()
                    )
                )
            }

            is MainAction.NavigateToControls -> {
                getScreen().dispose()
                setScreen(
                    ScreenWithUIStage(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        stage = ControlsStage(action.firstGame)
                    )
                )
            }

            MainAction.NavigateToAbout -> {
                getScreen().dispose()
                setScreen(
                    ScreenWithUIStage(
                        mainActionReceiver = ::onAction,
                        backgroundStage = backgroundStage,
                        stage = AboutStage()
                    )
                )
            }
        }
    }

    override fun render() {
        sPenHelperInstance.act()
        super.render()

        if (!firstFrameDrawn && getScreen() !is LoadingScreen){
            onAction(MainAction.FirstFrameDrawn)
        }
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize width: $width, height: $height, ratio: ${width.toFloat() / height}")
        backgroundStage?.onResize(width, height)
        super.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        getScreen().dispose()
        AssetsLoader.dispose()
    }

    companion object {
        private const val TAG = "Main"
    }
}
