package com.swarn.terminalapp.designsystem.util

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Int.pxToDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}

fun Int.pxToSp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale).toInt()
}
