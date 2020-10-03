package st235.com.github.flow_layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import st235.com.github.flow_layout.FlowLayout.Gravity.Companion.toGravity
import st235.com.github.flow_layout.layout.*
import st235.com.github.flow_layout.layout.LayoutDelegate
import st235.com.github.flow_layout.layout.RightLayoutDelegate
import st235.com.github.flow_layout.layout.RowInfo
import kotlin.math.max

typealias FlowLayoutParams = ViewGroup.MarginLayoutParams

class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    enum class Gravity(
        internal val internalId: Int
    ) {
        LEFT(0),
        RIGHT(1),
        CENTER(2),
        JUSTIFY(3),
        START(4),
        END(5);

        internal companion object {
            fun Int.toGravity(): Gravity =
                values().find { it.internalId == this } ?: throw IllegalArgumentException("Cannot find suitable gravity")
        }
    }

    private val layoutDelegate: LayoutDelegate

    private var rowsWidth = mutableListOf<RowInfo>()

    private val layoutDelegateFactory = LayoutDelegateFactory()

    init {
        setWillNotDraw(true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)

        val gravity = typedArray.getInt(R.styleable.FlowLayout_fl_gravity, Gravity.LEFT.internalId).toGravity()
        layoutDelegate = layoutDelegateFactory.create(gravity)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        rowsWidth.clear()

        val maxPaddedWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight

        var maxWidth = 0
        var maxHeight = 0

        var currentChildCount = 0
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

            currentChildCount += 1
            currentWidth += child.measuredWidth + horizontalMargins

            if (currentWidth > maxPaddedWidth) {  // wrap to a new line
                rowsWidth.add(RowInfo(currentWidth - (child.measuredWidth + horizontalMargins), currentChildCount - 1))
                maxHeight += currentHeight

                currentHeight = child.measuredHeight + verticalMargins
                currentWidth = child.measuredWidth + horizontalMargins
                currentChildCount = 1
            } else {  // stay on the same line
                currentHeight = max(currentHeight, child.measuredHeight + verticalMargins)
            }

            maxWidth = max(maxWidth, currentWidth)

            currentState = View.combineMeasuredStates(currentState, child.measuredState)
        }

        rowsWidth.add(RowInfo(currentWidth, currentChildCount))

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
        layoutDelegate.layout(this, rowsWidth, left, top, right, bottom)
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

    internal fun View.getChildLayoutParams(): FlowLayoutParams {
        val params = layoutParams
        val childParams = if (!checkLayoutParams(params)) {
            generateDefaultLayoutParams()
        } else {
            params
        }
        return childParams as FlowLayoutParams
    }
}