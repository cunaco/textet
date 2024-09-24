package com.example.textet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


// Кастомная цветовая схема для светлой темы
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF444444),    // Основной цвет
    onPrimary = Color.White,        // Цвет текста на основном цвете
    primaryContainer = Color(0xFFD4D4D4), // Фон для компонентов
    secondary = Color(0xFF000000),  // Второстепенный цвет (черный)
    onSecondary = Color.White,      // Цвет текста на второстепенном цвете
    background = Color(0xFFFFFFFF), // Цвет фона приложения (белый)
    surface = Color(0xFFFFFFFF),    // Поверхностный цвет (например, карточки)
    onSurface = Color(0xFF444444)   // Цвет текста на поверхностях
)

// Кастомная цветовая схема для темной темы
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBBBBBB),    // Основной цвет для темной темы
    onPrimary = Color.Black,        // Цвет текста на основном цвете
    primaryContainer = Color(0xFF333333), // Фон для компонентов
    secondary = Color(0xFF888888),  // Второстепенный цвет (темно-серый)
    onSecondary = Color.Black,      // Цвет текста на второстепенном цвете
    background = Color(0xFF121212), // Цвет фона для темной темы
    surface = Color(0xFF1E1E1E),    // Цвет поверхностей
    onSurface = Color(0xFFFFFFFF)   // Цвет текста на поверхностях
)

@Composable
fun textetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Используем стандартную типографику
        content = content
    )
}
