package com.ferit.robertoolujic.simplejamb

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ColorSlider(
    initialColorHue: Float,
    onColorChanged: (Float) -> Unit
) {
    var hueValue by remember(initialColorHue) { mutableFloatStateOf(initialColorHue) }
    val rainbowColors = listOf(
        Color.Red, Color.Yellow, Color.Green,
        Color.Cyan, Color.Blue, Color.Magenta, Color.Red
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Brush.horizontalGradient(rainbowColors))
    ) {
        Slider(
            value = hueValue,
            onValueChange = { newHue ->
                hueValue = newHue
                onColorChanged(newHue)
            },
            valueRange = 0f..360f,
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                thumbColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}