package st235.com.github.flowlayout

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import kotlinx.android.synthetic.main.activity_main.*
import st235.com.github.flow_layout.FlowLayoutParams


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tags = resources.getStringArray(R.array.cats_tags)

        for (tag in tags) {
            addChildTag(flowLayout, tag)
        }
    }

    private fun addChildTag(
        tagLayout: ViewGroup,
        tag: String
    ) {
        val tagView = TextView(ContextThemeWrapper(this, R.style.ChipViewTextAppearance))
        tagView.text = tag
        val params = FlowLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(2.toPx(), 4.toPx(), 2.toPx(), 4.toPx())
        tagLayout.addView(tagView, params)
    }
}
