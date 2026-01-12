package com.example.etherealwardrobe.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Pink Color Palette - Elegant & Modern
private val PinkPrimary = Color(0xFFE91E63) // Hot Pink
private val PinkSecondary = Color(0xFFF48FB1) // Light Pink
private val PinkTertiary = Color(0xFFFF4081) // Pink Accent
private val PinkContainer = Color(0xFFFCE4EC) // Very Light Pink
private val PinkSurface = Color(0xFFFFF0F5) // Lavender Blush

private val DarkColorScheme = darkColorScheme(
    primary = PinkPrimary,
    secondary = PinkSecondary,
    tertiary = PinkTertiary,
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2D2D2D),
    surfaceVariant = Color(0xFF3D3D3D),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFF5F5F5),
    onSurface = Color(0xFFE5E5E5),
    primaryContainer = Color(0xFF4A1E3D),
    onPrimaryContainer = PinkContainer
)

private val LightColorScheme = lightColorScheme(
    primary = PinkPrimary,
    secondary = PinkSecondary,
    tertiary = PinkTertiary,
    background = Color(0xFFFFFBFE),
    surface = Color.White,
    surfaceVariant = PinkSurface,
    onPrimary = Color.White,
    onSecondary = Color(0xFF4D1F3A),
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    primaryContainer = PinkContainer,
    onPrimaryContainer = Color(0xFF4D1F3A),
    outline = Color(0xFFE91E63).copy(alpha = 0.3f)
)

@Composable
fun EtherealWardrobeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}