package ru.etu.monitoring.utils.helpers

import android.animation.Animator
import android.view.animation.Animation

class AnimationListenerWrapper {
    internal var onRepeat: ((animation: Animation) -> Unit)? = null
    internal var onEnd: ((animation: Animation) -> Unit)? = null
    internal var onStart: ((animation: Animation) -> Unit)? = null

    fun onStart(block: ((animation: Animation) -> Unit)) {
        onStart = block
    }

    fun onEnd(block: ((animation: Animation) -> Unit)) {
        onEnd = block
    }

    fun onRepeat(block: ((animation: Animation) -> Unit)) {
        onRepeat = block
    }
}

class AnimatorListenerWrapper {
    internal var onStart: (() -> Unit)? = null
    internal var onEnd: (() -> Unit)? = null
    internal var onCancel: (() -> Unit)? = null
    internal var onRepeat: (() -> Unit)? = null

    fun onStart(block: (() -> Unit)) {
        onStart = block
    }

    fun onEnd(block: (() -> Unit)) {
        onEnd = block
    }

    fun onCancel(block: (() -> Unit)) {
        onCancel = block
    }

    fun onRepeat(block: (() -> Unit)) {
        onRepeat = block
    }
}

class AnimatorPauseListenerWrapper {
    internal var onPause: (() -> Unit)? = null
    internal var onResume: (() -> Unit)? = null

    fun onPause(block: (() -> Unit)) {
        onPause = block
    }

    fun onResume(block: (() -> Unit)) {
        onResume = block
    }
}

class OnPageChangeListenerWrapper {
    internal var onPageScrolled: ((Int, Float, Int) -> Unit)? = null
    internal var onPageSelected: ((Int) -> Unit)? = null
    internal var onPageScrollStateChanged: ((Int) -> Unit)? = null

    fun onPageScrolled(block: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) {
        onPageScrolled = block
    }

    fun onPageSelected(block: (Int) -> Unit) {
        onPageSelected = block
    }

    fun onPageScrollStateChanged(block: (Int) -> Unit) {
        onPageScrollStateChanged = block
    }
}

class AnimatePropsWrapper(private val animator: Animator?) {

    fun onEnd(block: () -> Unit) {
        if (animator == null) {
            block()
        } else {
            animator.addListener { onEnd { block() } }
        }
    }
}