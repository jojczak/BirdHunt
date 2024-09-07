package pl.jojczak.birdhunt.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import pl.jojczak.birdhunt.base.BaseScreen
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.screens.gameplay.stage.GameplayStage

class GameplayScreen(
    mainActionReceiver: (action: MainAction) -> Unit
) : BaseScreen<GameplayScreenAction>(
    mainActionReceiver = mainActionReceiver
) {
    private val gameplayStage = GameplayStage()

    override fun show() {
        Gdx.app.log(TAG, "show gameplay screen")
        super.show()
        Gdx.input.inputProcessor = gameplayStage
        gameplayStage.fadeIn()
    }

    override fun render(delta: Float) {
        super.render(delta)
        ScreenUtils.clear(Color.SKY)

        gameplayStage.act(delta)
        gameplayStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "resize GameplayScreen width: $width, height: $height")
        super.resize(width, height)
        gameplayStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        Gdx.app.log(TAG, "dispose GameplayScreen")
        super.dispose()
        gameplayStage.dispose()
    }

    companion object {
        private const val TAG = "GameplayScreen"
    }
}
