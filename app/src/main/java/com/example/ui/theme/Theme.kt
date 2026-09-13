package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Artisan Butcher Dark / Metal / Copper Scheme
private val ButcherDarkColorScheme = darkColorScheme(
    primary = CopperFlame,
    onPrimary = ObsidianBlack,
    primaryContainer = BurgundyPrimary,
    onPrimaryContainer = WarmCream,
    secondary = CopperLight,
    onSecondary = ObsidianBlack,
    secondaryContainer = BurgundyDeep,
    onSecondaryContainer = WarmCream,
    tertiary = CrimsonBright,
    onTertiary = Color.White,
    background = ObsidianBlack,
    onBackground = WarmCream,
    surface = CharcoalDark,
    onSurface = WarmCream,
    surfaceVariant = MetalSurface,
    onSurfaceVariant = AntiqueParchment,
    outline = MetalBorder,
    outlineVariant = Color(0xFF424755)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce our custom Rock/Metal & Kurdish palette for brand identity
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ButcherDarkColorScheme,
        typography = Typography,
        content = content
    )
}
