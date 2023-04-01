package com.example.calorietracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.example.core_ui.BrightGreen
import com.example.core_ui.DarkGray
import com.example.core_ui.DarkGreen
import com.example.core_ui.Dimensions
import com.example.core_ui.LightGray
import com.example.core_ui.LocalSpacing
import com.example.core_ui.MediumGray
import com.example.core_ui.Orange
import com.example.core_ui.TextWhite


private val DarkColorPalette = darkColors(
    primary = BrightGreen,
    primaryVariant = DarkGreen,
    secondary = Orange,
    background = MediumGray,
    onBackground = TextWhite,
    surface = LightGray,
    onSurface = TextWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColors(
    primary = BrightGreen,
    primaryVariant = DarkGreen,
    secondary = Orange,
    background = Color.White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
)


// the theme of the main activity when the whole code is putted in
@Composable
fun CalorieTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit // content inside the theme
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    // explained in Dimensions.kt

    // thanks to do this we can access the Dimensions file from anyplace in the code that is a composable
    // every provides we make in here is the default one for the content that lives under this tree
    //https://medium.com/geekculture/jetpack-compose-compositionlocal-what-you-need-to-know-979a4aef6412
    // if we use
/*    CompositionLocalProvider(LocalSpacing provides NewDimensions()) {
        // content that will live and use the value provided in this case the newDimensions instead of the default ones
    }*/

    // so in this case the material theme will make use of local spacing with the
    // dimensions provided, also if we use
    //LocalSpacing.current we make use of provided local composition either the default one or the new provided in the block

    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content // content inside this will use the provided dimensions which is the whole app
        )
    }
    // if we use the LocalSpacing outside the provides scope then will use the default dimensions
    // defines in val LocalSpacing = compositionLocalOf { Dimensions() }
    // see main activity for example
}