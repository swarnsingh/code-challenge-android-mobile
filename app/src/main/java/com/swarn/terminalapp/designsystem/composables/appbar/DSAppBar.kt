package com.swarn.terminalapp.designsystem.composables.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swarn.terminalapp.designsystem.composables.UIModePreviews
import com.swarn.terminalapp.designsystem.composables.theme.DSAppTheme
import com.swarn.terminalapp.designsystem.composables.theme.DSColor
import com.swarn.terminalapp.designsystem.composables.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DSAppBar(title: String = "", onBackClick: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text(
                text = title,
                color = DSColor.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(start = if (onBackClick != null) 24.dp else 16.dp),
                style = Typography.titleLarge
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        modifier = Modifier.testTag("BackButton"),
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = DSColor.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DSColor.AppBarColor
        )
    )
}

@UIModePreviews
@Composable
private fun DSAppBarPreview() {
    DSAppTheme {
        DSAppBar(title = "Title", onBackClick = {})
    }
}