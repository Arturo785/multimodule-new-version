package com.example.core_ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val default: Dp = 0.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceExtraLarge: Dp = 64.dp
)


// this means that the Dimensions will be available in the composibles three, like when using local
// context or things locally in the compose three

// every provides we make in here is the default one for the content that lives under this tree
//https://medium.com/geekculture/jetpack-compose-compositionlocal-what-you-need-to-know-979a4aef6412
// if we use
/*    CompositionLocalProvider(LocalSpacing provides NewDimensions()) {
        // content that will live and use the value provided in this case the newDimensions instead of the default ones
    }*/

// so in this case the material theme will make use of local spacing with the
// dimensions provided, also if we use
//LocalSpacing.current we make use of provided local composition either the default one or the new provided in the block

// we make this variable in order to provide an instance of what we want to add to our compositionLocal of
// and the default value we want to attach to it

// this is the first step, create an entity of our desired class to be provided by the compositionLocalOf and
// it's default value
val LocalSpacing = compositionLocalOf { Dimensions() }

// then we attach it to our compositionLocal provider with the value we want to be used by the child under the context of the
//CompositionLocalProvider

/*    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        // the things under this scope will use the provided value
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }*/
// this was done in the app theme file in order to it being provided to the app