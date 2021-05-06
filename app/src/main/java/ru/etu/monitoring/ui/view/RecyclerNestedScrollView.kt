package ru.etu.monitoring.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecyclerNestedScrollView : NestedScrollView {
    var rv: RecyclerView? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy < 0 && isRvScrolledToTop(rv) || dy > 0 && !isNsvScrolledToBottom(this)) {
            scrollBy(0, dy)
            consumed[1] = dy
            return
        }
        super.onNestedPreScroll(rv!!, dx, dy, consumed, type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedPreFling(target: View, velX: Float, velY: Float): Boolean {
        if (velY < 0 && isRvScrolledToTop(rv) || velY > 0 && !isNsvScrolledToBottom(this)) {
            fling(velY.toInt())
            return true
        }
        return super.onNestedPreFling(rv!!, velX, velY)
    }

    companion object {
        /**
         * Returns true iff the NestedScrollView is scrolled to the bottom of its
         * content (i.e. if the card's inner RecyclerView is completely visible).
         */
        private fun isNsvScrolledToBottom(nsv: NestedScrollView): Boolean {
            return !nsv.canScrollVertically(1)
        }

        private fun isNsvScrolledToTop(nsv: NestedScrollView): Boolean {
            return nsv.canScrollVertically(-1)
        }

        /**
         * Returns true iff the RecyclerView is scrolled to the top of its
         * content (i.e. if the RecyclerView's first item is completely visible).
         */
        private fun isRvScrolledToTop(rv: RecyclerView?): Boolean {
            if (rv!!.adapter == null) {
                return true
            }
            if (rv.adapter!!.itemCount == 0) {
                return true
            }
            val lm = rv.layoutManager ?: return true
            var firstVisiblePos: Int
            if (lm is LinearLayoutManager) {
                firstVisiblePos = lm.findFirstVisibleItemPosition()
                if (lm.findViewByPosition(0) == null) {
                    return false
                }
            } else if (lm is StaggeredGridLayoutManager) {
                val itemPositions = lm.findFirstVisibleItemPositions(null)
                firstVisiblePos = itemPositions[0]
                for (i in itemPositions) {
                    if (i < firstVisiblePos) {
                        firstVisiblePos = i
                    }
                }
                if (lm.findViewByPosition(0) == null) {
                    return false
                }
            } else {
                return true
            }
            var rvTop = rv.paddingTop
            val firstItem = lm.findViewByPosition(0)
            val top = firstItem!!.top
            if (firstItem.layoutParams is MarginLayoutParams) {
                rvTop += (firstItem.layoutParams as MarginLayoutParams).topMargin
            }
            return firstVisiblePos == 0 && top > 0 && top <= rvTop
        }
    }
}