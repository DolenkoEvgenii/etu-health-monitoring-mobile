package ru.etu.monitoring.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager

class ViewPagerCustomDuration : ViewPager {
    private var mScroller: ScrollerCustomDuration? = null

    constructor(context: Context) : super(context) {
        postInitViewPager()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        postInitViewPager()
    }

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = viewpager.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(context, interpolator.get(null) as Interpolator)
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }

    }

    fun setScrollDuration(duration: Int) {
        mScroller?.duration = duration
    }
}