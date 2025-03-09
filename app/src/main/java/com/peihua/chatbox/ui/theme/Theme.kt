package com.peihua.chatbox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Grey[900],
    onPrimary = White,
    primaryContainer = Blue[300],
    onPrimaryContainer = Black,
    inversePrimary = Black,
    secondary = Grey[900],
    onSecondary = White,
    secondaryContainer = Grey[900],
    onSecondaryContainer = White,
    tertiary = White80,
    onTertiary = White,
    tertiaryContainer = Grey[900],
    onTertiaryContainer = White,
    background = Grey[900],
    onBackground = White,
    surface = Grey[900],
    onSurface = White,
//    surfaceVariant = colorScheme.surfaceVariant,
//    onSurfaceVariant = colorScheme.onSurfaceVariant,
//    surfaceTint = colorScheme.surfaceTint,
//    inverseSurface = inverseSurface,
//    inverseOnSurface = inverseOnSurface,
    error = Color.Red,
    onError = White,
//    errorContainer = errorContainer,
//    onErrorContainer = onErrorContainer,
//    outline = outline,
//    outlineVariant = outlineVariant,
//    scrim = scrim,
//    surfaceBright = surfaceBright,
//    surfaceContainer = surfaceContainer,
//    surfaceContainerHigh = surfaceContainerHigh,
//    surfaceContainerHighest = surfaceContainerHighest,
//    surfaceContainerLow = surfaceContainerLow,
//    surfaceContainerLowest = surfaceContainerLowest,
//    surfaceDim = surfaceDim,
)

private val LightColorScheme = lightColorScheme(
    primary = White,
    onPrimary = Grey[800],
    primaryContainer = Blue[700],
    onPrimaryContainer = Grey[300],
    inversePrimary = White,
    secondary = White,
    onSecondary = Grey[500],
    secondaryContainer = Grey[300],
    onSecondaryContainer = Grey[800],
    tertiary = Black40,
    onTertiary = White,
    tertiaryContainer = White,
    onTertiaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
//    surfaceVariant = colorScheme.surfaceVariant,
//    onSurfaceVariant = colorScheme.onSurfaceVariant,
//    surfaceTint = colorScheme.surfaceTint,
    inverseSurface = Black,
    inverseOnSurface = White,
    error = Color.Red,
    onError = White,
//    errorContainer = errorContainer,
//    onErrorContainer = onErrorContainer,
//    outline = outline,
//    outlineVariant = outlineVariant,
//    scrim = scrim,
//    surfaceBright = surfaceBright,
//    surfaceContainer = surfaceContainer,
//    surfaceContainerHigh = surfaceContainerHigh,
//    surfaceContainerHighest = surfaceContainerHighest,
//    surfaceContainerLow = surfaceContainerLow,
//    surfaceContainerLowest = surfaceContainerLowest,
//    surfaceDim = surfaceDim,
)

@Composable
fun ChatBoxComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChatBoxTypography(),
        content = content,
    )
}