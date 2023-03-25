package com.example.core.util

import android.content.Context

// we use this utility class to access string resources from inside a viewModel or parts when we don't have a context
sealed class UiText {
    data class DynamicString(val text: String) : UiText() // just to be able to pass a normal string
    data class StringResource(val resId: Int) : UiText() // to be able to get string resources

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}