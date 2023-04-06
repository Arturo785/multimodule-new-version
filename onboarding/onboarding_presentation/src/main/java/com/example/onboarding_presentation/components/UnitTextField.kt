package com.example.onboarding_presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.core_ui.LocalSpacing


//Modifier.alignBy(LastBaseline)
//all the texts that should be align each other should have this modifier applied

@Composable
fun UnitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String, // the unit to display at left of the text
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle( // the bigger font used for the unit
        color = MaterialTheme.colors.primaryVariant,
        fontSize = 70.sp
    ),
) {
    val spacing = LocalSpacing.current

    // to put the text side by side
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number // only accepts numbers
            ),
            singleLine = true,
            modifier = Modifier
                .width(IntrinsicSize.Min) // will only use as much space as it really needs, makes center the value easier, like wrap content
                .alignBy(LastBaseline) // will align both texts as the same level no matter the difference in sizes
                .semantics { contentDescription = "unit text field" }
        )

        Spacer(modifier = Modifier.width(spacing.spaceSmall))

        Text(
            text = unit,
            modifier = Modifier.alignBy(LastBaseline) // will align both texts as the same level no matter the difference in sizes
        )
    }


}