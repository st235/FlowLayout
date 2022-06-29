package st235.com.github.flowlayout.layout

import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import st235.com.github.flowlayout.FlowLayout
import java.util.*

internal class LayoutDelegateFactory {

    fun create(gravity: FlowLayout.Gravity): LayoutDelegate {
        return when(gravity) {
            FlowLayout.Gravity.LEFT -> LeftLayoutDelegate()
            FlowLayout.Gravity.RIGHT -> RightLayoutDelegate()
            FlowLayout.Gravity.CENTER -> CenterLayoutDelegate()
            FlowLayout.Gravity.JUSTIFY -> JustifyLayoutDelegate()
            FlowLayout.Gravity.START -> {
                if (isLtr()) {
                    LeftLayoutDelegate()
                } else {
                    RightLayoutDelegate()
                }
            }
            FlowLayout.Gravity.END -> {
                if (isLtr()) {
                    RightLayoutDelegate()
                } else {
                    LeftLayoutDelegate()
                }
            }
        }
    }

    private fun isLtr(): Boolean {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
    }
}