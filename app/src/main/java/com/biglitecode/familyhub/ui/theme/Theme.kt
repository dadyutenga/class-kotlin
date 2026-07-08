package com.biglitecode.familyhub.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Warm dark variants — same family palette, deepened for dark mode.
private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = Color(0xFF0E2A1A),
    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,
    secondary = GoldYellow,
    onSecondary = Color(0xFF2A1F0A),
    secondaryContainer = Color(0xFF5A4718),
    onSecondaryContainer = Color.White,
    background = Color(0xFF1A140C),
    onBackground = Color(0xFFF3E7CF),
    surface = Color(0xFF241B10),
    onSurface = Color(0xFFF3E7CF),
    surfaceVariant = Color(0xFF3A2E1C),
    onSurfaceVariant = Color(0xFFE4D6B6),
    error = CoralRed,
    onError = Color.Black,
    errorContainer = Color(0xFF5C2323),
    onErrorContainer = Color(0xFFFFE0E0),
    outline = Color(0xFF7A6850),
    outlineVariant = Color(0xFF5A4A37)
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = ForestGreenLight,
    onPrimaryContainer = Color(0xFF0E2A1A),
    secondary = GoldYellow,
    onSecondary = TextBrown,
    secondaryContainer = GoldYellowLight,
    onSecondaryContainer = TextBrown,
    background = CreamBackground,
    onBackground = TextBrown,
    surface = CardCream,
    onSurface = TextBrown,
    surfaceVariant = GoldYellowLight,
    onSurfaceVariant = TextMutedBrown,
    error = CoralRed,
    onError = Color.White,
    errorContainer = Color(0xFFFBE3E3),
    onErrorContainer = Color(0xFF5C2323),
    outline = BorderGreen,
    outlineVariant = TextMutedBrown
)

@Composable
fun FamilyHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+; disabled by default so the
    // FamilyHub warm brand palette stays consistent across devices.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = FamilyHubShapes,
        content = content
    )
}