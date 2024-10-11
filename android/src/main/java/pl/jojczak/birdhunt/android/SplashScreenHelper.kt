package pl.jojczak.birdhunt.android

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.view.children
import com.badlogic.gdx.Gdx

class SplashScreenHelper(
    private val context: Context,
    splashScreen: SplashScreen
) {
    private lateinit var root: FrameLayout
    private var appLoaded = true

    init {
        splashScreen.setOnExitAnimationListener(this::startExitAnimation)
        splashScreen.setKeepOnScreenCondition { appLoaded }
    }

    fun startExitAnimation() {
        Gdx.app.log(TAG, "Starting exit animation")
        appLoaded = false
    }

    private fun startExitAnimation(splashScreenProvider: SplashScreenViewProvider) {
        root = (splashScreenProvider.view as FrameLayout).apply { fadeOutIconAndBranding() }

        val scrWidth = root.width.toFloat()
        val scrHeight = root.height.toFloat()

        val canvas = HolePunchView(scrWidth, scrHeight, context)

        val holeRadius = maxOf(scrWidth / 2, scrHeight / 2) * 1.15f
        ValueAnimator.ofFloat(1f, holeRadius).apply {
            setDuration(HOLE_ANIM_DURATION)
            addUpdateListener { update ->
                canvas.paint = getHolePunchPaint(scrWidth, scrHeight, update.animatedValue as Float)
            }
            doOnEnd {
                Gdx.app.log(TAG, "Splash exit animation finished")
                splashScreenProvider.remove()
            }
            start()
        }

        root.apply {
            addView(canvas, 0)
            setBackgroundColor(context.getColor(android.R.color.transparent))
        }
    }

    private fun FrameLayout.fadeOutIconAndBranding() {
        children.forEach { splashImage ->
            ObjectAnimator.ofFloat(splashImage, "alpha", 1f, 0f).apply {
                setDuration(ICON_ALPHA_ANIM_DURATION)
                start()
            }
        }
    }

    private fun getHolePunchPaint(
        scrWidth: Float,
        scrHeight: Float,
        holeRadius: Float
    ) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        shader = getRadialGradient(scrWidth, scrHeight, holeRadius)
    }

    private fun getRadialGradient(
        scrWidth: Float,
        scrHeight: Float,
        holeRadius: Float
    ) = RadialGradient(
        scrWidth / 2f,
        scrHeight / 2f,
        holeRadius,
        intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.BLACK),
        floatArrayOf(0f, 0.99f, 1f),
        Shader.TileMode.CLAMP
    )

    private class HolePunchView(
        private val scrWidth: Float,
        private val scrHeight: Float,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : View(
        context,
        attrs,
        defStyleAttr
    ) {
        var paint = Paint()
            set(value) {
                field = value
                invalidate()
            }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawRect(0f, 0f, scrWidth, scrHeight, paint)
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "SplashScreenHelper"

        private const val HOLE_ANIM_DURATION = 1000L
        private const val ICON_ALPHA_ANIM_DURATION = 500L
    }
}
