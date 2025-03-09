package com.peihua.chatbox.ui.theme

import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val White80 = Color(0xCCFFFFFF)
val Pink80 = Color(0xFFEFB8C8)

val Black = Color(0xFF000000)
val Black80 = Color(0xCC000000)
val Black40 = Color(0x66000000)
val Pink40 = Color(0xFF7D5260)

const val _greyPrimaryValue = 0xFF9E9E9E
val Grey: MaterialColor = MaterialColor(
    Color(_greyPrimaryValue), mapOf(
        50 to Color(0xFFFAFAFA),
        100 to Color(0xFFF5F5F5),
        200 to Color(0xFFEEEEEE),
        300 to Color(0xFFE0E0E0),
        350 to Color(0xFFD6D6D6), // only for raised button while pressed in light theme
        400 to Color(0xFFBDBDBD),
        500 to Color(_greyPrimaryValue),
        600 to Color(0xFF757575),
        700 to Color(0xFF616161),
        800 to Color(0xFF424242),
        850 to Color(0xFF303030), // only for background color in dark theme
        900 to Color(0xFF212121),
    )
)

val Blacks = MaterialColor(
    Black, mapOf(
        50 to Color(0x80000000),
        100 to Color(0xFFFFCDD2),
        200 to Color(0xFFEF9A9A),
        300 to Color(0xFFE57373),
        400 to Color(0xFFEF5350),
        500 to Black,
        600 to Color(0xFFE53935),
        700 to Color(0xFFD32F2F),
        800 to Color(0xFFC62828),
        900 to Color(0xFFB71C1C),
    )
)

const val _bluePrimaryValue = 0xFF2196F3;
val Blue: MaterialColor = MaterialColor(
    Color(_bluePrimaryValue), mapOf(
        50 to Color(0xFFE3F2FD),
        100 to Color(0xFFBBDEFB),
        200 to Color(0xFF90CAF9),
        300 to Color(0xFF64B5F6),
        400 to Color(0xFF42A5F5),
        500 to Color(_bluePrimaryValue),
        600 to Color(0xFF1E88E5),
        700 to Color(0xFF1976D2),
        800 to Color(0xFF1565C0),
        900 to Color(0xFF0D47A1),
    )
)
val red = MaterialColor(
    Color(_redPrimaryValue), mapOf(
        50 to Color(0xFFFFEBEE),
        100 to Color(0xFFFFCDD2),
        200 to Color(0xFFEF9A9A),
        300 to Color(0xFFE57373),
        400 to Color(0xFFEF5350),
        500 to Color(_redPrimaryValue),
        600 to Color(0xFFE53935),
        700 to Color(0xFFD32F2F),
        800 to Color(0xFFC62828),
        900 to Color(0xFFB71C1C),
    )
)
const val _redPrimaryValue = 0xFFF44336;

open class ColorSwitch<T>(val color: Color, val switch: Map<T, Color> = mapOf()) {

    operator fun get(key: T): Color {
        if (key == null) return color
        return switch[key] ?: color
    }

}

class MaterialColor(color: Color, switch: Map<Int, Color> = mapOf()) : ColorSwitch<Int>(color, switch)