package com.swarn.terminalapp.designsystem.composables

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swarn.terminalapp.designsystem.composables.keyboard.KeyboardState
import com.swarn.terminalapp.designsystem.composables.theme.DSAppTheme
import com.swarn.terminalapp.designsystem.composables.theme.DSColor
import com.swarn.terminalapp.designsystem.util.pxToDp
import kotlinx.coroutines.launch

@Composable
fun DSScreen(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    adjustResizeKeyboardBehavior: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    val paddingBottom = remember { mutableStateOf(0.dp) }
    val coroutineScope = rememberCoroutineScope()
    var keyboardModifier: Modifier = modifier

    if (adjustResizeKeyboardBehavior) {
        when {
            Build.VERSION.SDK_INT >= 30 ->
                keyboardModifier = Modifier
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .imePadding()

            else -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        KeyboardState.keyboardStateFlow.collect { data ->
                            paddingBottom.value = if (data.visible) data.height.pxToDp() else 0.dp
                        }
                    }
                }
            }
        }
    }
    DSAppTheme {
        Scaffold(
            modifier = modifier
                .padding(bottom = paddingBottom.value)
                .then(keyboardModifier),
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            content = content,
            contentWindowInsets = WindowInsets.statusBars,
            containerColor = DSColor.White,
        )
    }
}
