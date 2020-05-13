package st235.com.github.flow_layout.layout

internal class CenterLayoutDelegate: OffsetLayoutDelegate() {

    override fun offsetFromLeft(width: Int, rowsWidth: List<RowInfo>, row: Int): Int {
        return (width - rowsWidth[row].width) / 2
    }
}
