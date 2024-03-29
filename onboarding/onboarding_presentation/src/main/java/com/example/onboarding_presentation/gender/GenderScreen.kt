package com.example.onboarding_presentation.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.util.UiEvent
import com.example.core_ui.LocalSpacing
import com.example.core.R
import com.example.core.domain.model.Gender
import com.example.onboarding_presentation.components.ActionButton
import com.example.onboarding_presentation.components.SelectableButton

@Composable
fun GenderScreen(
    onNavigate: (UiEvent.Navigate) -> Unit, // instead of passing the navigation graph
    viewModel: GenderViewModel = hiltViewModel() // attaches the viewModel to scope the viewModel to the screen instead of to the navigation graph
) {
    val spacing = LocalSpacing.current

    // fires once because the key does not change, and thanks to the inner coroutine generated inside keeps listening into the events
    // if the key changes the inner coroutine gets cancelled and launched again
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    // the whole container of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge) // keeps from avoid touching the borders
    ) {

        // to stack the text and the buttons
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_gender),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            // the buttons side by side
            Row {
                SelectableButton(
                    text = stringResource(id = R.string.male),
                    isSelected = viewModel.selectedGender.collectAsState().value is Gender.Male, // checks the state of the selection
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGenderClick(Gender.Male)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    text = stringResource(id = R.string.female),
                    isSelected = viewModel.selectedGender.collectAsState().value is Gender.Female, // checks the state of the selection
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGenderClick(Gender.Female)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick, // sets the reference of the on click to the action of the viewModel
            modifier = Modifier.align(Alignment.BottomCenter) // the bottom of the screen
        )
    }
}