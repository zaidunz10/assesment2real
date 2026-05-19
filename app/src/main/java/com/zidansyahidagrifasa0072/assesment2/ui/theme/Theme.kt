package com.zidansyahidagrifasa0072.assesment2.ui.theme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CustomBlue,
    background = SolidBlack,
    surface = CardDark,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CustomBlue,
    background = LightGrayBg,
    surface = CardLight,
    onBackground = DeepDarkBlue,
    onSurface = DeepDarkBlue
)

@Composable
fun CctvManagerTheme(
    darkTheme: Boolean = false,
    colorOption: String = "Blue",
    content: @Composable () -> Unit
) {
    val basePrimary = when (colorOption) {
        "Purple" -> CustomPurple
        "Teal" -> CustomTeal
        else -> CustomBlue
    }

    val colors = if (darkTheme) {
        DarkColorScheme.copy(primary = basePrimary)
    } else {
        LightColorScheme.copy(primary = basePrimary)
    }

    androidx.compose.material3.MaterialTheme(
        colorScheme = colors,
        content = content
    )
}