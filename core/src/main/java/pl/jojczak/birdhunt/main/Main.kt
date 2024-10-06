package pl.jojczak.birdhunt.main

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.screens.about.AboutScreen
import pl.jojczak.birdhunt.screens.gameplay.GameplayScreen
import pl.jojczak.birdhunt.screens.loading.LoadingScreen
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreen
import pl.jojczak.birdhunt.screens.settings.SettingsScreen
import pl.jojczak.birdhunt.utils.SPenHelper
import pl.jojczak.birdhunt.utils.sPenHelperInstance

class Main : Game() {
    override fun create() {
        if (sPenHelperInstance.implType == SPenHelper.ImplType.DESKTOP) {
            sPenHelperInstance.connect({}, {})
        }

        setScreen(LoadingScreen(::onAction))
    }

    private fun onAction(action: MainAction) {
        Gdx.app.log(TAG, "Action received: $action")

        when (action) {
            MainAction.NavigateToMainMenu -> {
                getScreen().dispose()
                setScreen(MainMenuScreen(::onAction))
            }

            MainAction.NavigateToGameplay -> {
                getScreen().dispose()
                setScreen(GameplayScreen(::onAction))
            }

            MainAction.NavigateToSettings -> {
                getScreen().dispose()
                setScreen(SettingsScreen(::onAction))
            }

            MainAction.NavigateToAbout -> {
                getScreen().dispose()
                setScreen(AboutScreen(::onAction))
            }
        }
    }

    override fun render() {
        sPenHelperInstance.act()
        super.render()
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
