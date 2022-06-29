package st235.com.github.flowlayout.layout

import st235.com.github.flowlayout.FlowLayout

internal interface LayoutDelegate {
    fun layout(parent: FlowLayout,
               widths: List<RowInfo>,
               left: Int, top: Int,
               right: Int, bottom: Int)
}
