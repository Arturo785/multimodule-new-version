package com.example.tracker_presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core_ui.LocalSpacing

@Composable
fun SearchTextField(
    text: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(id = R.string.search),
    shouldShowHint: Boolean = false,
    onFocusChanged: (FocusState) -> Unit // FocusState comes from compose
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = { value -> onValueChange.invoke(value) },
            singleLine = true, // only write in one line
            keyboardActions = KeyboardActions(
                // this is the little search icon that can be triggered inside the keyboard, we override the action
                onSearch = {
                    onSearch.invoke() // the action to be done
                    defaultKeyboardAction(ImeAction.Search) // the one to override and make sure when doing it closes the keyboard
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search // to allow the search icon to be displayed on the keyboard
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp)) // make it round
                .padding(2.dp) // push inwards to make the shadow visible
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(spacing.spaceMedium) // pushes content inwards
                .padding(end = spacing.spaceMedium) // more padding in the end needed to not crash with icon
                .onFocusChanged { onFocusChanged.invoke(it) } // our callback
                .testTag("search_textfield")
        )
        // the hint is just a plain text over the basicTextField that is shown depending on the state passed as parameter
        if (shouldShowHint) {
            Text(
                text = hint,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light,
                color = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = spacing.spaceMedium)
            )
        }
        // icon to trigger the search as well as the keyboard one
        IconButton(
            onClick = { onSearch.invoke() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        }
    }
}