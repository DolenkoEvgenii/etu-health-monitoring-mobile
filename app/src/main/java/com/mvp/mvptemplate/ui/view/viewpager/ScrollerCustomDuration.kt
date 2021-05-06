package com.mvp.mvptemplate.ui.view.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class ScrollerCustomDuration : Scroller {
    private var mDuration = 880

    constructor(context: Context) : super(context)

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

    @SuppressLint("NewApi")
    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator, flywheel)

    /**
     * Set the factor by which the duration will change
     */
    fun setDuration(duration: Int) {
        mDuration = duration
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

}