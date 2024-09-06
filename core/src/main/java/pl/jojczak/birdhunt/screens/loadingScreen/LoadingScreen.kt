package pl.jojczak.birdhunt.screens.loadingScreen

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction

class LoadingScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<LoadingAction>(
    mainActionReceiver = mainActionReceiver
) {
    override fun show() {
        super.show()
        Gdx.app.log(TAG, "show")

        AssetsLoader.reset()
        AssetsLoader.startLoading()
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.app.log(TAG, "render, loading progress: ${AssetsLoader.progress()}")

        if (AssetsLoader.finished()) {
            mainActionReceiver(MainAction.NavigateToMainMenu)
        }
    }

    companion object {
        private const val TAG = "LoadingScreen"
    }
}
