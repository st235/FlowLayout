package st235.com.github.flowlayout.layout

internal class LeftLayoutDelegate: OffsetLayoutDelegate() {

    override fun offsetFromLeft(width: Int, rowsWidth: List<RowInfo>, row: Int): Int {
        return 0
    }
}