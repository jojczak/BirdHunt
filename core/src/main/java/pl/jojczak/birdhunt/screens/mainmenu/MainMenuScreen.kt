package pl.jojczak.birdhunt.screens.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction

class MainMenuScreen(
    mainActionReceiver: (action: MainAction) -> Unit
): BaseScreen(
    mainActionReceiver = mainActionReceiver
) {
    private val mainMenuStage = MainMenuStage()

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = mainMenuStage
    }

    override fun render(delta: Float) {
        super.render(delta)
        ScreenUtils.clear(Color.BLACK)

        mainMenuStage.act(delta)
        mainMenuStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        mainMenuStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()
        mainMenuStage.dispose()
    }
}
