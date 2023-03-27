package com.example.onboarding_presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.core.R
import com.example.core.navigation.Route
import com.example.core.util.UiEvent
import com.example.core_ui.LocalSpacing
import com.example.onboarding_presentation.components.ActionButton


@Composable
fun WelcomeScreen(
    onNextClick: () -> Unit  // this callback is used instead of passing the navController into the composable which is a little bit more clean
) {
    //val context = LocalContext.current // this way we can access the context in a composable
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),// comes from core resources
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium)) //comes from the localSpacing we did on the theme and core-Ui

        // this one we did on the components package
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = {
                onNextClick.invoke()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}