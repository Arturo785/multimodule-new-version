package com.example.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.example.core_ui.CarbColor
import com.example.core_ui.FatColor
import com.example.core_ui.ProteinColor


/*
*
* This Animatable function creates a float value holder that automatically animates
*  its value when the value is changed via animateTo. Animatable supports value change during an ongoing value change animation.
*  When that happens, a new animation will transition Animatable from its current value
*  (i.e. value at the point of interruption) to the new target.
*
* */

// this are the bars that get filled with the food macros (the rectangular ones)

@Composable
fun NutrientsBar(
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    calorieGoal: Int,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colors.background
    val caloriesExceedColor = MaterialTheme.colors.error

    // all of these hold the state of the animation on the UI for the macros
    val carbWidthRatio = remember {
        Animatable(0f)
    }
    val proteinWidthRatio = remember {
        Animatable(0f)
    }
    val fatWidthRatio = remember {
        Animatable(0f)
    }

    // are in separate launchedEffect because only one should react when one changes instead of all
    // remember that when the key changes the code inside gets executed, also contains a coroutine inside
    LaunchedEffect(key1 = carbs) {
        // animates from the current animatable value into the new one automatically and smooth
        carbWidthRatio.animateTo(
            targetValue = ((carbs * 4f) / calorieGoal) // formula for carbs
        )
    }
    LaunchedEffect(key1 = protein) {
        // animates from the current animatable value into the new one automatically and smooth
        proteinWidthRatio.animateTo(
            targetValue = ((protein * 4f) / calorieGoal) // formula for protein
        )
    }

    LaunchedEffect(key1 = fat) {
        // animates from the current animatable value into the new one automatically and smooth
        fatWidthRatio.animateTo(
            targetValue = ((fat * 9f) / calorieGoal)
        )
    }

    // time to draw into the UI
    Canvas(modifier = modifier) {
        // we haven't exceeded the amount of calories
        if (calories <= calorieGoal) {
            // we get the percentage of bar to fill depending on the size of the canvas width and our ratio of macros
            val carbsWidth = carbWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            //drawRoundRect = Draws a rounded rectangle with the given Paint

            // Basically we create 4 rectangles and paint in top of each other
            // this is the main one that has the background
            drawRoundRect(
                color = background,
                size = size, // takes whole size
                cornerRadius = CornerRadius(100f) // full round
            )

            // first macro to be painted
            // the one that represents fat, gets painted taking into account the width amount sum of all the macros
            // because the other ones paint over the fat one we need to sum all to make it stand from the others
            // is the right most one
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            // second macro to be painted
            // the protein one needs the width of the carbs one and it's own to be visible on not eaten by the carb one
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            // last macro to be painted, only needs the carbs width because paints over the ones painted before
            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

        } else { // we have exceeded the amount and all gets in red painted
            // Draws a rounded rectangle with the given Paint
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = CornerRadius(100f) // to make it totally round rectangle
            )
        }

    }

}