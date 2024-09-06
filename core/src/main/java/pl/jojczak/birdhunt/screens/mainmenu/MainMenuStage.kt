package pl.jojczak.birdhunt.screens.mainmenu

import pl.jojczak.birdhunt.base.BaseStage
import pl.jojczak.birdhunt.base.BaseUIStage
import pl.jojczak.birdhunt.screens.mainmenu.actors.LogoActor

class MainMenuStage: BaseStage() {
    private val logoActor = LogoActor()

    init {
        addActor(logoActor)
    }
}
