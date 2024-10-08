package pl.jojczak.birdhunt.screens.gameplay.stages.ui.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import pl.jojczak.birdhunt.assetsloader.Asset.Companion.FONT_180_BORDERED

class CountdownLabel(
    private val i18N: I18NBundle,
    skin: Skin
): Table() {
    private val countdownLabel = Label("3", skin, FONT_180_BORDERED, Color.WHITE)

    init {
        setFillParent(true)
        add(countdownLabel)
    }

    override fun act(delta: Float) {
        super.act(delta)

    }
}
