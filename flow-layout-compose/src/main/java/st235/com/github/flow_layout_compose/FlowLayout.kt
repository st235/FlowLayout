package st235.com.github.flow_layout_compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun FlowLayout(
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

        var rowWidth = 0
        var rowHeight = 0

        var height = 0

        placeables.forEach { placeable ->
            if (rowWidth + placeable.width > constraints.maxWidth) {
                height += rowHeight
                rowHeight = 0
                rowWidth = 0
            }

            rowWidth += placeable.width
            rowHeight = kotlin.math.max(rowHeight, placeable.height)
        }

        layout(constraints.maxWidth, height) {

            rowWidth = 0
            rowHeight = 0

            height = 0

            placeables.forEach { placeable ->
                if (rowWidth + placeable.width > constraints.maxWidth) {
                    height += rowHeight
                    rowHeight = 0
                    rowWidth = 0
                }

                placeable.placeRelative(x = rowWidth, y = height)

                rowWidth += placeable.width
                rowHeight = kotlin.math.max(rowHeight, placeable.height)
            }

        }
    }
}
