package pl.jojczak.birdhunt.assetsloader

enum class Asset {
    I18N,
    UI_SKIN,
    UI_BULLET,
    UI_S_PEN,
    TX_LOGO,
    TX_BIRD,
    TX_SCOPE,
    TX_SHOTGUN,
    TX_BULLET;

    companion object {
        const val FONT_46 = "PixelifySans-46"
        const val FONT_75 = "PixelifySans-75"
        const val FONT_75_BORDERED = "PixelifySans-75-bordered"
        const val FONT_180_BORDERED = "PixelifySans-180-bordered"
    }
}
