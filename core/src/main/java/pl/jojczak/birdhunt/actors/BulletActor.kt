package pl.jojczak.birdhunt.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import pl.jojczak.birdhunt.assetsloader.Asset
import pl.jojczak.birdhunt.assetsloader.AssetsLoader
import pl.jojczak.birdhunt.base.BaseActor
import pl.jojczak.birdhunt.screens.gameplay.GameplayHelper
import pl.jojczak.birdhunt.screens.gameplay.GameplayState

class BulletActor(
    private val gameplayHelper: GameplayHelper,
    private val startPos: Vector2,
    private val endPos: Vector2,
    angle: Float
) : BaseActor() {
    private val texture = AssetsLoader.get<Texture>(Asset.TX_BULLET)

    init {
        setSize(
            texture.width.toFloat(),
            texture.height.toFloat()
        )

        rotation = angle

        setPosition(
            startPos.x,
            startPos.y
        )
    }

    override fun act(delta: Float) {
        if (gameplayHelper.getState() !is GameplayState.Playing) return
        super.act(delta)
    }

    override fun onStage() {
        super.onStage()

        val flyDuration = FLY_TIME_FOR_300 * (startPos.dst(endPos) / 300f)

        addAction(SequenceAction(
            MoveToAction().apply {
                x = endPos.x
                y = endPos.y
                duration = flyDuration
            },
            RunnableAction().apply {
                setRunnable {
                    remove()
                }
            }
        ))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(texture, x - width / 2, y - height / 2, width / 2, height / 2, width, height, scaleX, scaleY, rotation, 0, 0, texture.width, texture.height, false, false)
    }

    companion object {
        private const val FLY_TIME_FOR_300 = 0.15f
    }
}
