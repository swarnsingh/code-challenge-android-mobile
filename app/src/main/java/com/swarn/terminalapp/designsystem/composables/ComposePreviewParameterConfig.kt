package com.swarn.terminalapp.designsystem.composables

import androidx.compose.runtime.Composable

fun interface ComposePreviewParameterConfig<T> {
    @Composable
    fun value(): T
}
