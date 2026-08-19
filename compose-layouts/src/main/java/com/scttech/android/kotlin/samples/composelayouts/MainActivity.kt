package com.scttech.android.kotlin.samples.composelayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ComposeLayouts()
                }
            }
        }
    }
}

/**
 * A scrollable catalog of the core Jetpack Compose layout building blocks:
 * [Column], [Row], [Box], weighted children, and the lazy (recycling) list
 * equivalents [LazyColumn] and [LazyRow].
 */
@Composable
fun ComposeLayouts() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Compose Layouts",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "A tour of the layout composables you'll reach for most often.",
            style = MaterialTheme.typography.bodyMedium,
        )

        LayoutSection(
            title = "Column",
            description = "Stacks children vertically. Arrangement controls spacing " +
                "along the vertical axis, horizontalAlignment across it.",
        ) {
            ColumnDemo()
        }

        LayoutSection(
            title = "Row",
            description = "Stacks children horizontally, the counterpart to Column.",
        ) {
            RowDemo()
        }

        LayoutSection(
            title = "Box",
            description = "Stacks children on top of one another. Each child can be " +
                "aligned independently within the Box.",
        ) {
            BoxDemo()
        }

        LayoutSection(
            title = "Weighted Row",
            description = "Modifier.weight() divides remaining space between " +
                "siblings proportionally, like flex-grow.",
        ) {
            WeightedRowDemo()
        }

        LayoutSection(
            title = "LazyRow",
            description = "Horizontally scrolling list that only composes the items " +
                "currently on screen, ideal for long or unbounded lists.",
        ) {
            LazyRowDemo()
        }

        LayoutSection(
            title = "LazyColumn",
            description = "The vertical equivalent of LazyRow. Given a fixed height " +
                "here so it can live inside the outer scrolling Column.",
        ) {
            LazyColumnDemo()
        }
    }
}

/**
 * Wraps a layout demo in a titled [Card] so each example reads as its own
 * entry in the catalog.
 */
@Composable
private fun LayoutSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
            content()
        }
    }
}

@Composable
private fun ColumnDemo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LabeledBox("1", Color(0xFF6750A4))
        LabeledBox("2", Color(0xFF7D5260))
        LabeledBox("3", Color(0xFF386A20))
    }
}

@Composable
private fun RowDemo() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        LabeledBox("1", Color(0xFF6750A4))
        LabeledBox("2", Color(0xFF7D5260))
        LabeledBox("3", Color(0xFF386A20))
    }
}

@Composable
private fun BoxDemo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
    ) {
        LabeledBox("TopStart", Color(0xFF6750A4), modifier = Modifier.align(Alignment.TopStart))
        LabeledBox("Center", Color(0xFF7D5260), modifier = Modifier.align(Alignment.Center))
        LabeledBox("BottomEnd", Color(0xFF386A20), modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun WeightedRowDemo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        LabeledBox("1x", Color(0xFF6750A4), modifier = Modifier.weight(1f).fillMaxSize())
        LabeledBox("2x", Color(0xFF7D5260), modifier = Modifier.weight(2f).fillMaxSize())
        LabeledBox("1x", Color(0xFF386A20), modifier = Modifier.weight(1f).fillMaxSize())
    }
}

@Composable
private fun LazyRowDemo() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(sampleItems) { (label, color) ->
            LabeledBox(label, color, modifier = Modifier.size(56.dp))
        }
    }
}

@Composable
private fun LazyColumnDemo() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(sampleItems) { (label, color) ->
            LabeledBox(label, color, modifier = Modifier.fillMaxWidth().height(40.dp))
        }
    }
}

/** Ten label/color pairs shared by the lazy-list demos above. */
private val sampleItems: List<Pair<String, Color>> = (1..10).map { index ->
    "Item $index" to Color.hsv(hue = index * 36f, saturation = 0.5f, value = 0.85f)
}

/**
 * A small colored surface with centered text, used as a stand-in "content
 * block" across the demos above so the layout behavior — not the content —
 * stays the focus.
 */
@Composable
private fun LabeledBox(
    label: String,
    color: Color,
    modifier: Modifier = Modifier.size(64.dp),
) {
    Box(
        modifier = modifier.background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeLayoutsPreview() {
    MaterialTheme {
        ComposeLayouts()
    }
}
