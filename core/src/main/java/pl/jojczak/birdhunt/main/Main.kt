package pl.jojczak.birdhunt.main

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.screens.about.AboutScreen
import pl.jojczak.birdhunt.screens.gameplay.GameplayScreen
import pl.jojczak.birdhunt.screens.loading.LoadingScreen
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreen
import pl.jojczak.birdhunt.screens.settings.SettingsScreen
import pl.jojczak.birdhunt.stages.background.BackgroundStage
import pl.jojczak.birdhunt.utils.SPenHelper
import pl.jojczak.birdhunt.utils.sPenHelperInstance

class Main : Game() {
    private var backgroundStage: BackgroundStage? = null

    override fun create() {
        if (sPenHelperInstance.implType == SPenHelper.ImplType.DESKTOP) {
            sPenHelperInstance.connect({}, {})
        }

        setScreen(LoadingScreen(::onAction))
    }

    private fun onAction(action: MainAction) {
        Gdx.app.log(TAG, "Action received: $action")

        when (action) {
            MainAction.SetupBackgroundStage -> {
                backgroundStage = BackgroundStage().apply {
                    fadeIn()
                    onResize(Gdx.graphics.width, Gdx.graphics.height)
                }
            }

            MainAction.NavigateToMainMenu -> {
                getScreen().dispose()
                setScreen(MainMenuScreen(::onAction, backgroundStage))
            }

            MainAction.NavigateToGameplay -> {
                getScreen().dispose()
                setScreen(GameplayScreen(::onAction, backgroundStage))
            }

            MainAction.NavigateToSettings -> {
                getScreen().dispose()
                setScreen(SettingsScreen(::onAction, backgroundStage))
            }

            MainAction.NavigateToAbout -> {
                getScreen().dispose()
                setScreen(AboutScreen(::onAction, backgroundStage))
            }
        }
    }

    override fun render() {
        sPenHelperInstance.act()
        super.render()
    }

    override fun resize(width: Int, height: Int) {
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
