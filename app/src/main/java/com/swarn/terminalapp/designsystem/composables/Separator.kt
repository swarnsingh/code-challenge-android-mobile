package com.swarn.terminalapp.designsystem.composables

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swarn.terminalapp.designsystem.composables.theme.DSColor


@Composable
fun Separator(
    modifier: Modifier = Modifier,
    thickness: Dp = 0.5.dp,
    color: Color = DSColor.Purple200,
) {
    HorizontalDivider(modifier = modifier, thickness = thickness, color = color)
}