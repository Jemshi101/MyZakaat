package `com`.quartzbit.myzakaat.util.decorators

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View

/**
 * Created by Jemsheer K D on 28 April, 2018.
 * Package `com`.quartzbit.myzakaat.util.decorators
 * Project Dearest
 */
class StaggeredGridItemDecoration(private val borderSize: Int,
                                  private val borderColor: ColorDrawable) :
        androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val lm = parent.layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager
        val lp = view.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams

        val spanCount = lm.spanCount
        val spanIndex = lp.spanIndex
        val position = parent.getChildAdapterPosition(view)

        if (spanIndex == 0) {
            // left edge
            // should write left border
            outRect.left = borderSize
        }

        if (position < spanCount) {
            // first row
            // should write top border
            outRect.top = borderSize
        }

        outRect.right = borderSize
        outRect.bottom = borderSize
    }

    override fun onDrawOver(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        if (parent.childCount == 0) {
            return
        }
        (0 until parent.childCount).forEach { index ->
            val child = parent.getChildAt(index)
            val lp = child.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
            val spanIndex = lp.spanIndex

            val childTop = child.top - lp.topMargin
            val childBottom = child.bottom + lp.bottomMargin
            val childLeft = child.left
            val childRight = child.right

            if (spanIndex == 0) {
                // draw left horizontal border
                val leftBorderRight = childLeft - lp.leftMargin
                val leftBorderLeft = leftBorderRight - borderSize
                borderColor.setBounds(leftBorderLeft, childTop, leftBorderRight, childBottom + borderSize)
                borderColor.draw(c)
            }

            // draw right horizontal border
            val rightBorderLeft = childRight + lp.rightMargin
            val rightBorderRight = rightBorderLeft + borderSize
            borderColor.setBounds(rightBorderLeft, childTop, rightBorderRight, childBottom + borderSize)
            borderColor.draw(c)

            // draw bottom border
            val bottomBorderTop = childBottom
            val bottomBorderBottom = bottomBorderTop + borderSize
            borderColor.setBounds(childLeft, bottomBorderTop, childRight, bottomBorderBottom)
            borderColor.draw(c)
        }
    }
}