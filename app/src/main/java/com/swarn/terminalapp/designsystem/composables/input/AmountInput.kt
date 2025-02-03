package com.swarn.terminalapp.designsystem.composables.input

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swarn.terminalapp.designsystem.composables.Separator
import com.swarn.terminalapp.designsystem.composables.UIModePreviews
import com.swarn.terminalapp.designsystem.composables.theme.DSColor

const val MAX_LENGTH = 10

@Composable
fun AmountInput(
    value: TextFieldValue,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(128.dp)
            .focusRequester(focusRequester),
        contentAlignment = Alignment.BottomCenter
    ) {
        BasicTextField(
            value = value.text,
            onValueChange = {
                if (it.length <= MAX_LENGTH) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .testTag("AmountInput")
                .focusable(),
            textStyle = textStyle.copy(
                color = DSColor.Purple200,
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            keyboardActions = keyboardActions,
            cursorBrush = SolidColor(DSColor.Purple200),
            singleLine = true,
        )
        Separator(thickness = 2.dp)
    }
}

@Composable
@UIModePreviews
private fun AmountInputPreview() {
    AmountInput(
        value = TextFieldValue("123"),
        onValueChange = {},
    )
}
