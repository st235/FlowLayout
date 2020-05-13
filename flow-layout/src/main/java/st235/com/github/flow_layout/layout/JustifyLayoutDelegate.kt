package st235.com.github.flow_layout.layout

import android.view.View
import st235.com.github.flow_layout.FlowLayout
import kotlin.math.max

internal class JustifyLayoutDelegate: LayoutDelegate {

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
            var currentChildCount = 0
            var justifyWidth = getJustifyWidth(layoutWidth,widths[currentRow].width, widths[currentRow].childCount)
            var currentLeft = layoutLeft
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
                currentChildCount++

                // if current left is bigger than possible right bound
                // we should wrap to a new line
                if (currentLeft + childWidth > layoutRight) {
                    currentRow++
                    currentLeft = layoutLeft
                    currentTop += maxRowHeight
                    justifyWidth = getJustifyWidth(layoutWidth,widths[currentRow].width, widths[currentRow].childCount)
                    maxRowHeight = 0
                    currentChildCount = 1
                }

                child.layout(
                    currentLeft + params.leftMargin,
                    currentTop + params.topMargin,
                    currentLeft + child.measuredWidth + params.leftMargin,
                    currentTop + child.measuredHeight + params.topMargin
                )

                maxRowHeight = max(maxRowHeight, childHeight)

                val averageWidth = (widths[currentRow].width / widths[currentRow].childCount)
                val offset = if (justifyWidth < averageWidth) justifyWidth else 0
                currentLeft += childWidth + offset
            }
        }
    }

    private fun getJustifyWidth(layoutWidth: Int, rowWidth: Int, childCount: Int): Int {
        if (childCount <= 1) {
            return layoutWidth - rowWidth
        }

        return (layoutWidth - rowWidth) / (childCount - 1)
    }
}