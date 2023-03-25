package com.example.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.tracker_presentation.components.UnitDisplay


@Composable
fun NutrientBarInfo(
    value: Int,
    goal: Int,
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
) {
    val background = MaterialTheme.colors.background
    val goalExceededColor = MaterialTheme.colors.error

    // this is needed for animating the percentage value of the circle degrees up to 360
    val angleRatio = remember {
        Animatable(0f)
    }

    // if we receive a new value gets recomposed and recalculated
    LaunchedEffect(key1 = value) {
        angleRatio.animateTo(
            targetValue = if (goal > 0) {
                value / goal.toFloat() // our ratio
            } else 0f, // if we are beyond the goal no need to animate
            // this modifies the duration of the animation
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }

    // basically we need a column with two texts
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        // to draw

        // first we draw the circle to later put the texts inside of it
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // makes it square
        ) {

            // this is the main circle which is either white or full or red because of error
            // drawArc basically draws a circle if sweepAngle is give up to 360
            drawArc(
                color = if (value <= goal) background else goalExceededColor, // if exceeded then put it all in red
                startAngle = 0f,
                sweepAngle = 360f, //
                useCenter = false, // means that we don't want to connect to the center but to be hollow
                size = size,
                style = Stroke( // our stroke to paint outside
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // if we did not exceeded the goal we draw another exact circle but now with the values
            // we want to animate
            if (value <= goal) {
                drawArc(
                    color = color, // the color of our macro
                    startAngle = 90f,
                    sweepAngle = 360f * angleRatio.value, // to give our ratio the circle anim
                    useCenter = false, // means that we don't want to connect to the center but to be hollow
                    size = size,
                    style = Stroke( // to fill our macro
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        // we stop drawing BUT still in the box

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // the composable we made
            UnitDisplay(
                amount = value,
                unit = stringResource(id = R.string.grams),
                amountColor = if (value <= goal) {
                    MaterialTheme.colors.onPrimary
                } else goalExceededColor,
                unitColor = if (value <= goal) {
                    MaterialTheme.colors.onPrimary
                } else goalExceededColor
            )

            Text(
                text = name, // the macro to show
                color = if (value <= goal) {
                    MaterialTheme.colors.onPrimary
                } else goalExceededColor, // red if exceeded
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light
            )
        }
    }


}