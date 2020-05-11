package st235.com.github.flow_layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

typealias FlowLayoutParams = ViewGroup.MarginLayoutParams

class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    init {
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxPaddedWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight

        var maxWidth = 0
        var maxHeight = 0

        var currentState = 0
        var currentWidth = 0
        var currentHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childParams = child.getChildLayoutParams()

            if (child.visibility == View.GONE) {
                continue
            }

            val horizontalMargins = childParams.leftMargin + childParams.rightMargin
            val verticalMargins = childParams.topMargin + childParams.bottomMargin

            measureChildWithMargins(
                child,
                widthMeasureSpec, 0,
                heightMeasureSpec, 0
            )

            currentWidth += child.measuredWidth + horizontalMargins

            if (currentWidth > maxPaddedWidth) {  // wrap to a new line
                maxHeight += currentHeight

               currentHeight = child.measuredHeight + verticalMargins
                currentWidth = child.measuredWidth + horizontalMargins
            } else {  // stay on the same line
                currentHeight = max(currentHeight, child.measuredHeight + verticalMargins)
            }

            maxWidth = max(maxWidth, currentWidth)

            currentState = View.combineMeasuredStates(currentState, child.measuredState)
        }

        // the last line was not counted
        // as it was not wrapped
        maxHeight += currentHeight

        maxHeight = max(maxHeight, suggestedMinimumHeight)
        maxWidth = max(maxWidth, suggestedMinimumWidth)

        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, currentState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                currentState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    override fun onLayout(
        changed: Boolean,
        left: Int, top: Int,
        right: Int, bottom: Int
    ) {
        val layoutLeft = paddingLeft
        val layoutRight = measuredWidth - paddingRight

        val layoutTop = paddingTop
        val layoutBottom = measuredHeight - paddingBottom

        val layoutWidth = layoutRight - layoutLeft
        val layoutHeight = layoutBottom - layoutTop

        var maxRowHeight = 0

        var currentLeft = layoutLeft
        var currentTop = layoutTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.getChildLayoutParams()

            if (child.visibility == View.GONE) {
                continue
            }

            child.measure(
                MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.AT_MOST)
            )

            val childWidth = child.measuredWidth + params.leftMargin + params.rightMargin
            val childHeight = child.measuredHeight + params.topMargin + params.bottomMargin

            // if current left is bigger than possible right bound
            // we should wrap to a new line
            if (currentLeft + childWidth > layoutRight) {
                currentLeft = layoutLeft
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

    override fun checkLayoutParams(p: LayoutParams): Boolean = p is FlowLayoutParams

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams
            = FlowLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams): LayoutParams = FlowLayoutParams(p)

    override fun generateDefaultLayoutParams(): LayoutParams =
        FlowLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

    private fun View.getChildLayoutParams(): FlowLayoutParams {
        val params = layoutParams
        val childParams = if (!checkLayoutParams(params)) {
            generateDefaultLayoutParams()
        } else {
            params
        }
        return childParams as FlowLayoutParams
    }
}