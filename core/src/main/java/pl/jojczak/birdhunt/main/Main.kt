package pl.jojczak.birdhunt.main

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.screens.gameplay.GameplayScreen
import pl.jojczak.birdhunt.screens.loadingScreen.LoadingScreen
import pl.jojczak.birdhunt.screens.mainmenu.MainMenuScreen

class Main : Game() {
    override fun create() {
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
        }
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
