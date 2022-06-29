package st235.com.github.flowlayout.layout

import android.view.View
import st235.com.github.flowlayout.FlowLayout
import kotlin.math.max

internal abstract class OffsetLayoutDelegate: LayoutDelegate {

    override fun layout(parent: FlowLayout, widths: List<RowInfo>, left: Int, top: Int, right: Int, bottom: Int) {
        with (parent) {
            val layoutLeft = paddingLeft
            val layoutRight = measuredWidth - paddingRight

            val layoutTop = paddingTop
            val layoutBottom = measuredHeight - paddingBottom

            val layoutWidth = layoutRight - layoutLeft
            val layoutHeight = layoutBottom - layoutTop

            var maxRowHeight = 0

            var currentRow = 0
            var currentLeft = layoutLeft + offsetFromLeft(layoutWidth, widths, 0)
            var currentTop = layoutTop

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val params = child.getChildLayoutParams()

                if (child.visibility == View.GONE) {
                    continue
                }

                child.measure(
                    View.MeasureSpec.makeMeasureSpec(layoutWidth, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(layoutHeight, View.MeasureSpec.AT_MOST)
                )

                val childWidth = child.measuredWidth + params.leftMargin + params.rightMargin
                val childHeight = child.measuredHeight + params.topMargin + params.bottomMargin

                // if current left is bigger than possible right bound
                // we should wrap to a new line
                if (currentLeft + childWidth > layoutRight) {
                    currentRow++
                    currentLeft = layoutLeft + offsetFromLeft(layoutWidth, widths, currentRow)
                    currentTop += maxRowHeight
                    maxRowHeight = 0
                }

                child.layout(
                    currentLeft + params.leftMargin,
                    currentTop + params.topMargin,
                    currentLeft + child.measuredWidth + params.leftMargin,
                    currentTop + child.measuredHeight + params.topMargin
                )

                maxRowHeight = max(maxRowHeight, childHeight)
                currentLeft += childWidth
            }
        }
    }

    abstract fun offsetFromLeft(width: Int, rowsWidth: List<RowInfo>, row: Int): Int
}