package com.mvp.mvptemplate.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import com.mvp.mvptemplate.R
import com.mvp.mvptemplate.utils.helpers.setHeight
import com.mvp.mvptemplate.utils.helpers.setWidth

class AspectRatioFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var aspectRatio: Float = 0.toFloat()
    private var aspectRatioEnabled: Boolean = false
    private var dominantMeasurement: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout)
        aspectRatio = a.getFloat(R.styleable.AspectRatioFrameLayout_aspectRatio, DEFAULT_ASPECT_RATIO)
        aspectRatioEnabled = a.getBoolean(R.styleable.AspectRatioFrameLayout_aspectRatioEnabled, DEFAULT_ASPECT_RATIO_ENABLED)
        dominantMeasurement = a.getInt(R.styleable.AspectRatioFrameLayout_dominantMeasurement, DEFAULT_DOMINANT_MEASUREMENT)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!aspectRatioEnabled) return

        val newWidth: Int
        val newHeight: Int
        when (dominantMeasurement) {
            MEASUREMENT_WIDTH -> {
                newWidth = measuredWidth
                newHeight = (newWidth * aspectRatio).toInt()
            }

            MEASUREMENT_HEIGHT -> {
                newHeight = measuredHeight
                newWidth = (newHeight / aspectRatio).toInt()
            }

            else -> throw IllegalStateException("Unknown measurement with ID $dominantMeasurement")
        }

        setMeasuredDimension(newWidth, newHeight)

        when (dominantMeasurement) {
            MEASUREMENT_WIDTH -> {
                if (newHeight > 0) {
                    fixChildrenHeightIfNeeded(newHeight)
                }
            }

            MEASUREMENT_HEIGHT -> {
                if (newWidth > 0) {
                    fixChildrenWidthIfNeeded(newWidth)
                }
            }
        }

    }

    private fun fixChildrenHeightIfNeeded(height: Int) {
        children.forEach { childView ->
            if (childView.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                childView.setTag(R.id.TAG_ONLINE_ID, "yeah")
            }

            if (childView.getTag(R.id.TAG_ONLINE_ID) == "yeah") {
                if (childView.height != height) {
                    childView.setHeight(height)

                    handler.post {
                        this.invalidate()

                        childView.requestLayout()
                        childView.invalidate()
                    }
                }
            }
        }
    }

    private fun fixChildrenWidthIfNeeded(width: Int) {
        children.forEach { childView ->
            if (childView.layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                childView.setTag(R.id.TAG_ONLINE_ID, "yeah")
            }

            if (childView.getTag(R.id.TAG_ONLINE_ID) == "yeah") {
                if (childView.width != width) {
                    childView.setWidth(width)

                    handler.post {
                        this.invalidate()

                        childView.requestLayout()
                        childView.invalidate()
                    }
                }
            }
        }
    }

    /** Get the aspect ratio for this image view.  */
    fun getAspectRatio(): Float {
        return aspectRatio
    }

    /** Set the aspect ratio for this image view. This will update the view instantly.  */
    fun setAspectRatio(aspectRatio: Float) {
        this.aspectRatio = aspectRatio
        if (aspectRatioEnabled) {
            requestLayout()
        }
    }

    /** Get whether or not forcing the aspect ratio is enabled.  */
    fun getAspectRatioEnabled(): Boolean {
        return aspectRatioEnabled
    }

    /** set whether or not forcing the aspect ratio is enabled. This will re-layout the view.  */
    fun setAspectRatioEnabled(aspectRatioEnabled: Boolean) {
        this.aspectRatioEnabled = aspectRatioEnabled
        requestLayout()
    }

    /** Get the dominant measurement for the aspect ratio.  */
    fun getDominantMeasurement(): Int {
        return dominantMeasurement
    }

    /**
     * Set the dominant measurement for the aspect ratio.
     *
     * @see .MEASUREMENT_WIDTH
     *
     * @see .MEASUREMENT_HEIGHT
     */
    fun setDominantMeasurement(dominantMeasurement: Int) {
        if (dominantMeasurement != MEASUREMENT_HEIGHT && dominantMeasurement != MEASUREMENT_WIDTH) {
            throw IllegalArgumentException("Invalid measurement type.")
        }
        this.dominantMeasurement = dominantMeasurement
        requestLayout()
    }

    companion object {
        // NOTE: These must be kept in sync with the AspectRatioImageView attributes in attrs.xml.
        val MEASUREMENT_WIDTH = 0
        val MEASUREMENT_HEIGHT = 1

        private val DEFAULT_ASPECT_RATIO = 1f
        private val DEFAULT_ASPECT_RATIO_ENABLED = false
        private val DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH
    }
}