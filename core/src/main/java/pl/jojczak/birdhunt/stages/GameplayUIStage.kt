package pl.jojczak.birdhunt.stages

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import pl.jojczak.birdhunt.actors.shotgunactor.ShotgunActor
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.ui.HitWindow
import pl.jojczak.birdhunt.ui.RoundWindow
import pl.jojczak.birdhunt.ui.ScoreWidget
import pl.jojczak.birdhunt.ui.ShotWindow

class GameplayUIStage : BaseUIStage() {
    private val scoreWidget = ScoreWidget(i18N, skin)
    private val pauseButton = TextButton("||", skin)
    private val shotWindow = ShotWindow(i18N, skin)
    private val roundWindow = RoundWindow(i18N, skin)
    private val hitWindow = HitWindow(i18N, skin)

    private val leftGroup = Table().apply {
        add(pauseButton).size(CELL_SIZE).padLeft(PAD)
        add(shotWindow).size(CELL_SIZE).padLeft(PAD)
    }

    private val rightGroup = Table().apply {
        add(hitWindow).size(CELL_SIZE).padRight(PAD)
        add(roundWindow).size(CELL_SIZE).padRight(PAD)
    }

    private val bottomContainer = Table().apply {
        val bottomPad = (ShotgunActor.FRAME_HEIGHT.toGameSize() - CELL_SIZE) / 2f

        setFillParent(true)
        bottom()
        add(leftGroup).expandX().left().padBottom(bottomPad)
        add(rightGroup).expandX().right().padBottom(bottomPad)
    }

    init {
        addActor(scoreWidget)
        addActor(bottomContainer)
        shotWindow.shots = 3
    }

    companion object {
        private const val CELL_SIZE = 190f
        private const val PAD = 20f
    }
}
