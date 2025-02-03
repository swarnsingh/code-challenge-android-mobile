package com.swarn.terminalapp.designsystem.composables

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "LightMode", uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
annotation class UIModePreviews

@Preview(name = "LightMode", uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class NormalModePreviews
