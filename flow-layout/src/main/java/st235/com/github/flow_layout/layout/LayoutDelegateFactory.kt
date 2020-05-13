package st235.com.github.flow_layout.layout

import st235.com.github.flow_layout.FlowLayout

internal class LayoutDelegateFactory {

    fun create(gravity: FlowLayout.Gravity): LayoutDelegate {
        return when(gravity) {
            FlowLayout.Gravity.LEFT -> LeftLayoutDelegate()
            FlowLayout.Gravity.RIGHT -> RightLayoutDelegate()
            FlowLayout.Gravity.CENTER -> CenterLayoutDelegate()
            FlowLayout.Gravity.JUSTIFY -> JustifyLayoutDelegate()
        }
    }
}