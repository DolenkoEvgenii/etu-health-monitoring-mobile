package com.mvp.mvptemplate.utils.helpers

import android.animation.ObjectAnimator
import android.os.Handler
import android.view.animation.LinearInterpolator
import android.widget.HorizontalScrollView

fun HorizontalScrollView.maxScrollRight() {
    Handler().post {
        val child = getChildAt(0) ?: return@post
        val childWidth = child.measuredWidth
        val scrollWidth = width - paddingLeft - paddingRight

        val scrollToX = childWidth - scrollWidth
        if (scrollToX > 0) {
            val animator = ObjectAnimator.ofInt(this, "scrollX", scrollToX)
            animator.interpolator = LinearInterpolator()
            animator.duration = 300
            animator.start()
        }
    }
}

fun HorizontalScrollView.maxScrollLeft() {
    val animator = ObjectAnimator.ofInt(this, "scrollX", 0)
    animator.interpolator = LinearInterpolator()
    animator.duration = 300
    animator.start()
}