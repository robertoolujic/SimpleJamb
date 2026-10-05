package com.ferit.robertoolujic.simplejamb.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
enum class AppTheme { SYSTEM, LIGHT, DARK }
@Composable
fun SimpleJambTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    primaryColor: Float = 300f,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(primaryColor,1f,1f)))
    val isDark = when (theme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }
    val colorScheme = if (isDark)
        darkColorScheme(
            primary = primaryColor,
            secondary = Color(0xFFFFFFFF),
            tertiary = Color(0xFF777777)
        ) else lightColorScheme(
        primary = primaryColor,
        secondary = Color(0xFF000000),
        tertiary = Color(0xFF777777)
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}