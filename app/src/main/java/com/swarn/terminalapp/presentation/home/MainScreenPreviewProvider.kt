package com.swarn.terminalapp.presentation.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.designsystem.composables.ComposePreviewParameterConfig
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.model.TransactionType

class MainScreenPreviewProvider :
    PreviewParameterProvider<ComposePreviewParameterConfig<out ResultState<Transaction>>> {

    private val loadingState = ComposePreviewParameterConfig {
        ResultState.Loading(data = null)
    }

    private val errorState = ComposePreviewParameterConfig {
        ResultState.Error(data = null, throwable = Throwable("Something went wrong"))
    }

    private val successState = ComposePreviewParameterConfig {
        ResultState.Success(
            Transaction(
                isTransactionSuccessful = true,
                totalBalance = 100.0.toBigDecimal(),
                transactionType = TransactionType.CREDIT
            )
        )
    }

    override val values: Sequence<ComposePreviewParameterConfig<out ResultState<Transaction>>> =
        sequenceOf(
            successState,
            errorState,
            loadingState,
        ) as Sequence<ComposePreviewParameterConfig<out ResultState<Transaction>>>
}