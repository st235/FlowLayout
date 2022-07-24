package st235.com.github.flowlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import st235.com.github.flowlayout.compose.FlowLayout
import st235.com.github.flowlayout.compose.FlowLayoutDirection
import st235.com.github.flowlayout.ui.theme.FlowLayoutTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Root()
                }
            }
        }
    }
}

@Composable
fun Root() {
    val scroll = rememberScrollState()

    FlowLayout(
        direction = FlowLayoutDirection.START,
        modifier = Modifier
            .padding(top = 16.dp)
            .verticalScroll(scroll)
    ) {
        for (catName in stringArrayResource(id = R.array.cats_tags)) {
            Text(
                text = catName,
                modifier = Modifier
                    .padding(8.dp, 4.dp)
                    .background(colorResource(id = R.color.colorGreyOp80), RoundedCornerShape(4.dp))
                    .padding(8.dp, 4.dp),
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.padding(2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowLayoutTheme {
        Root()
    }
}