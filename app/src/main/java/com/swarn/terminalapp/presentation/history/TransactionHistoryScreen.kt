package com.swarn.terminalapp.presentation.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.swarn.terminalapp.R
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.designsystem.composables.ComposePreviewParameterConfig
import com.swarn.terminalapp.designsystem.composables.DSErrorView
import com.swarn.terminalapp.designsystem.composables.DSScreen
import com.swarn.terminalapp.designsystem.composables.Separator
import com.swarn.terminalapp.designsystem.composables.UIModePreviews
import com.swarn.terminalapp.designsystem.composables.appbar.DSAppBar
import com.swarn.terminalapp.designsystem.composables.progressbar.LoadingOverlay
import com.swarn.terminalapp.designsystem.composables.theme.DSAppTheme
import com.swarn.terminalapp.domain.model.TransactionHistory

@Composable
fun TransactionHistoryScreen(
    transactionHistoryState: ResultState<List<TransactionHistory>>,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    val isLoading = transactionHistoryState is ResultState.Loading

    DSScreen(
        adjustResizeKeyboardBehavior = true,
        topBar = {
            DSAppBar(stringResource(id = R.string.history)) {
                onBackClick.invoke()
            }
        },
    ) { paddingValues ->
        when (transactionHistoryState) {
            is ResultState.Loading -> {
                LoadingOverlay(isLoading = isLoading)
            }

            is ResultState.Success -> {
                HistoryScreenContent(paddingValues, transactionHistoryState.data)
            }

            is ResultState.Error -> {
                DSErrorView(
                    description = transactionHistoryState.throwable.localizedMessage,
                    primaryButtonOnClick = {
                        onRetryClick.invoke()
                    }
                )
            }
        }
    }
}

@Composable
private fun HistoryScreenContent(
    paddingValues: PaddingValues,
    transactionHistory: List<TransactionHistory>,
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .fillMaxSize(),
    ) {
        items(
            items = transactionHistory,
            key = { transaction -> transaction.id }
        ) { transaction ->
            TransactionHistoryCardView(transaction = transaction)
            Separator(
                modifier = Modifier.padding(
                    start = 16.dp,
                )
            )
        }
    }
}

@Composable
@UIModePreviews
private fun TransactionHistoryScreenPreview(
    @PreviewParameter(TransactionHistoryScreenPreviewProvider::class) previewConfig: ComposePreviewParameterConfig<ResultState<List<TransactionHistory>>>,
) {
    DSAppTheme {
        TransactionHistoryScreen(
            transactionHistoryState = previewConfig.value(),
            onBackClick = {},
            onRetryClick = {},
        )
    }
}