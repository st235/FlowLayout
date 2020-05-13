package st235.com.github.flow_layout.layout

import st235.com.github.flow_layout.FlowLayout

internal interface LayoutDelegate {
    fun layout(parent: FlowLayout,
               widths: List<RowInfo>,
               left: Int, top: Int,
               right: Int, bottom: Int)
}