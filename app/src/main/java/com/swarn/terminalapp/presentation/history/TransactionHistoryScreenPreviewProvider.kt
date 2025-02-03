package com.swarn.terminalapp.presentation.history

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.designsystem.composables.ComposePreviewParameterConfig
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.model.TransactionType

class TransactionHistoryScreenPreviewProvider :
    PreviewParameterProvider<ComposePreviewParameterConfig<out ResultState<List<TransactionHistory>>>> {

    private val loadingState = ComposePreviewParameterConfig {
        ResultState.Loading(data = null)
    }

    private val errorState = ComposePreviewParameterConfig {
        ResultState.Error(data = null, throwable = Throwable("Something went wrong"))
    }

    private val successState = ComposePreviewParameterConfig {
        ResultState.Success(transactionHistoryList)
    }
    override val values: Sequence<ComposePreviewParameterConfig<out ResultState<List<TransactionHistory>>>> =
        sequenceOf(
            successState,
            errorState,
            loadingState,
        ) as Sequence<ComposePreviewParameterConfig<out ResultState<List<TransactionHistory>>>>

    val transactionHistoryList = listOf(
        TransactionHistory(
            id = 1,
            amount = 10.0.toBigDecimal(),
            dateAndTime = "2024-05-04, 10:00 AM",
            transactionType = TransactionType.CREDIT
        ),
        TransactionHistory(
            id = 2,
            amount = 20.0.toBigDecimal(),
            dateAndTime = "2024-05-05, 1:00 PM",
            transactionType = TransactionType.CREDIT
        ),
        TransactionHistory(
            id = 3,
            amount = 30.0.toBigDecimal(),
            dateAndTime = "2024-05-06, 3:00 PM",
            transactionType = TransactionType.DEBIT
        ),
        TransactionHistory(
            id = 4,
            amount = 15.5.toBigDecimal(),
            dateAndTime = "2024-05-07, 9:00 AM",
            transactionType = TransactionType.DEBIT
        ),
        TransactionHistory(
            id = 5,
            amount = 50.0.toBigDecimal(),
            dateAndTime = "2024-05-08, 12:30 PM",
            transactionType = TransactionType.CREDIT
        ),
        TransactionHistory(
            id = 6,
            amount = 25.75.toBigDecimal(),
            dateAndTime = "2024-05-09, 5:00 PM",
            transactionType = TransactionType.DEBIT
        ),
        TransactionHistory(
            id = 7,
            amount = 100.0.toBigDecimal(),
            dateAndTime = "2024-05-10, 11:00 AM",
            transactionType = TransactionType.CREDIT
        ),
        TransactionHistory(
            id = 8,
            amount = 60.0.toBigDecimal(),
            dateAndTime = "2024-05-11, 2:15 PM",
            transactionType = TransactionType.DEBIT
        ),
        TransactionHistory(
            id = 9,
            amount = 5.0.toBigDecimal(),
            dateAndTime = "2024-05-12, 4:45 PM",
            transactionType = TransactionType.CREDIT
        ),
        TransactionHistory(
            id = 10,
            amount = 12.5.toBigDecimal(),
            dateAndTime = "2024-05-13, 6:30 PM",
            transactionType = TransactionType.DEBIT
        )
    )
}