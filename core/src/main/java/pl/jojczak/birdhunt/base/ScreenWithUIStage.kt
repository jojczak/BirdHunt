package pl.jojczak.birdhunt.base

import com.badlogic.gdx.Gdx
import pl.jojczak.birdhunt.main.MainAction
import pl.jojczak.birdhunt.stages.background.BackgroundStage

class ScreenWithUIStage (
    mainActionReceiver: (action: MainAction) -> Unit,
    backgroundStage: BackgroundStage?,
    private val stage: ScreenStage
): BaseScreen<MainAction>(
    mainActionReceiver,
    backgroundStage
) {
    override fun show() {
        Gdx.app.log(TAG, "Show")

        super.show()
        stage.mainActionReceiver = { mainActionReceiver(it) }
        stage.fadeIn()
        Gdx.app.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        super.render(delta)

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stage.onResize(width, height)
    }

    override fun dispose() {
        stage.dispose()
        super.dispose()
    }

    abstract class ScreenStage: BaseUIStage() {
        var mainActionReceiver: (action: MainAction) -> Unit = {}
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "ScreenWithOneStage"
    }
}
