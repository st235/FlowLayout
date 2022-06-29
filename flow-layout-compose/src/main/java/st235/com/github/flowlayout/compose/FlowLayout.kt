package st235.com.github.flowlayout.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import st235.com.github.flowlayout.compose.OffsetMode.Companion.calculateOffset
import st235.com.github.flowlayout.compose.OffsetMode.Companion.multiplier
import java.util.Locale
import kotlin.math.max

private data class RowInfo(
    val width: Int,
    val height: Int,
    val itemsCount: Int
)

private enum class OffsetMode {
    START,
    END,
    CENTER;

    companion object {

        val OffsetMode.multiplier: Int
        get() {
            return when (this) {
                START -> 1
                CENTER -> 1
                END -> -1
            }
        }

        fun OffsetMode.calculateOffset(
            itemWidth: Int,
            rowWidth: Int,
            maxWidth: Int
        ): Int {
            return when (this) {
                START -> 0
                END -> maxWidth - itemWidth
                CENTER -> (maxWidth - rowWidth) / 2
            }
        }

    }
}

@Composable
private fun AdjustableFlowLayout(
    offsetMode: OffsetMode,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val rowInfos = mutableListOf<RowInfo>()

        var rowWidth = 0
        var rowHeight = 0
        var itemsCount = 0

        var height = 0

        placeables.forEach { placeable ->
            if (rowWidth + placeable.width > constraints.maxWidth) {
                rowInfos.add(RowInfo(rowWidth, rowHeight, itemsCount))

                height += rowHeight
                rowHeight = 0
                rowWidth = 0
                itemsCount = 0
            }

            rowWidth += placeable.width
            rowHeight = max(rowHeight, placeable.height)
            itemsCount += 1
        }

        // last row
        rowInfos.add(RowInfo(rowWidth, rowHeight, itemsCount))

        layout(constraints.maxWidth, height) {

            var currentRowIndex = 0
            var currentRowWidth = 0

            height = 0

            placeables.forEach { placeable ->
                if (currentRowWidth + placeable.width > rowInfos[currentRowIndex].width) {
                    height += rowInfos[currentRowIndex].height
                    currentRowWidth = 0
                    currentRowIndex += 1
                }

                placeable.placeRelative(
                    x = offsetMode.multiplier * currentRowWidth
                            + offsetMode.calculateOffset(
                                itemWidth = placeable.width,
                                rowWidth = rowInfos[currentRowIndex].width,
                                maxWidth = constraints.maxWidth
                            ),
                    y = height
                )

                currentRowWidth += placeable.width
            }

        }
    }
}

@Composable
private fun JustifyFlowLayout(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val rowInfos = mutableListOf<RowInfo>()

        var rowWidth = 0
        var rowHeight = 0
        var itemsCount = 0

        var height = 0

        placeables.forEach { placeable ->
            if (rowWidth + placeable.width > constraints.maxWidth) {
                rowInfos.add(RowInfo(rowWidth, rowHeight, itemsCount))

                height += rowHeight
                rowHeight = 0
                rowWidth = 0
                itemsCount = 0
            }

            rowWidth += placeable.width
            rowHeight = max(rowHeight, placeable.height)
            itemsCount += 1
        }

        // last row
        rowInfos.add(RowInfo(rowWidth, rowHeight, itemsCount))

        layout(constraints.maxWidth, height) {

            var currentRowIndex = 0
            var currentRowWidth = 0
            var itemCount = 0

            height = 0

            placeables.forEach { placeable ->
                if (currentRowWidth + placeable.width > constraints.maxWidth) {
                    height += rowInfos[currentRowIndex].height
                    currentRowWidth = 0
                    currentRowIndex += 1
                    itemCount = 0
                }

                placeable.placeRelative(
                    x = currentRowWidth,
                    y = height
                )

                val count = rowInfos[currentRowIndex].itemsCount
                val spacing = if (count > 1 && (itemCount < rowInfos[currentRowIndex].itemsCount - 1)) {
                    (constraints.maxWidth - rowInfos[currentRowIndex].width) / (count - 1)
                } else {
                    0
                }

                currentRowWidth += placeable.width + spacing
                itemCount += 1
            }

        }
    }
}

@Composable
private fun LeftFlowLayout(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AdjustableFlowLayout(
        offsetMode = OffsetMode.START,
        modifier = modifier,
        content = content
    )
}

@Composable
private fun RightFlowLayout(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AdjustableFlowLayout(
        offsetMode = OffsetMode.END,
        modifier = modifier,
        content = content
    )
}

@Composable
private fun CenterFlowLayout(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AdjustableFlowLayout(
        offsetMode = OffsetMode.CENTER,
        modifier = modifier,
        content = content
    )
}

enum class FlowLayoutDirection {
    LEFT,
    START,
    RIGHT,
    END,
    CENTER,
    JUSTIFY
}

@Composable
fun FlowLayout(
    direction: FlowLayoutDirection = FlowLayoutDirection.LEFT,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    when (direction) {
        FlowLayoutDirection.LEFT -> LeftFlowLayout(modifier = modifier, content = content)
        FlowLayoutDirection.START -> if (isLtr()) LeftFlowLayout(modifier = modifier, content = content) else RightFlowLayout(modifier = modifier, content = content)
        FlowLayoutDirection.RIGHT -> RightFlowLayout(modifier = modifier, content = content)
        FlowLayoutDirection.END -> if (isLtr()) RightFlowLayout(modifier = modifier, content = content) else LeftFlowLayout(modifier = modifier, content = content)
        FlowLayoutDirection.CENTER -> CenterFlowLayout(modifier = modifier, content = content)
        FlowLayoutDirection.JUSTIFY -> JustifyFlowLayout(modifier = modifier, content = content)
    }
}

private fun isLtr(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
}

