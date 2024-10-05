package pl.jojczak.birdhunt.screens.gameplay.stages.world.actors.shotgunactor

sealed class ShotgunAngle(val degrees: Float) {
    class Right(degrees: Float) : ShotgunAngle(degrees)
    class SlRight(degrees: Float) : ShotgunAngle(degrees)
    class Center(degrees: Float) : ShotgunAngle(degrees)
    class SlLeft(degrees: Float) : ShotgunAngle(degrees)
    class Left(degrees: Float) : ShotgunAngle(degrees)

    companion object {
        private const val MAX_ANGLE = 90f
        private const val MED_ANGLE = 30f
        private const val LOW_ANGLE = 10f

        fun getAngle(angle: Float) = when (angle) {
            in -MAX_ANGLE..-MED_ANGLE -> Right(angle)
            in -MED_ANGLE..-LOW_ANGLE -> SlRight(angle)
            in LOW_ANGLE..MED_ANGLE -> SlLeft(angle)
            in MED_ANGLE..MAX_ANGLE -> Left(angle)
            else -> Center(angle)
        }
    }
}
