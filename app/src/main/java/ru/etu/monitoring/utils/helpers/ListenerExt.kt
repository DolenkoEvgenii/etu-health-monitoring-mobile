package ru.etu.monitoring.utils.helpers

import android.animation.Animator
import android.os.Build
import android.view.animation.Animation
import androidx.viewpager.widget.ViewPager

fun Animation.addListener(init: AnimationListenerWrapper.() -> Unit) {
    val wrapper = AnimationListenerWrapper().apply { init() }
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {
            wrapper.onRepeat?.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation) {
            wrapper.onEnd?.invoke(animation)
        }

        override fun onAnimationStart(animation: Animation) {
            wrapper.onStart?.invoke(animation)
        }
    })
}

fun Animator.addListener(init: AnimatorListenerWrapper.() -> Unit) {
    val wrapper = AnimatorListenerWrapper().apply { init() }
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
            wrapper.onRepeat?.invoke()
        }

        override fun onAnimationEnd(p0: Animator?) {
            wrapper.onEnd?.invoke()
        }

        override fun onAnimationCancel(p0: Animator?) {
            wrapper.onCancel?.invoke()
        }

        override fun onAnimationStart(p0: Animator?) {
            wrapper.onStart?.invoke()
        }

    })
}

fun Animator.addPauseListener(init: AnimatorPauseListenerWrapper.() -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val wrapper = AnimatorPauseListenerWrapper().apply { init() }
        addPauseListener(object : Animator.AnimatorPauseListener {
            override fun onAnimationPause(p0: Animator?) {
                wrapper.onPause?.invoke()
            }

            override fun onAnimationResume(p0: Animator?) {
                wrapper.onResume?.invoke()
            }
        })
    }
}

fun ViewPager.addOnPageChangeListener(init: OnPageChangeListenerWrapper.() -> Unit) {
    val wrapper = OnPageChangeListenerWrapper().apply { init() }
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            wrapper.onPageScrollStateChanged?.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            wrapper.onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            wrapper.onPageSelected?.invoke(position)
        }

    })
}