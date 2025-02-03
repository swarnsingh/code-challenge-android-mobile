package com.swarn.terminalapp.domain.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

@Immutable
enum class TransactionType {
    DEBIT,
    CREDIT,
    CHECK_BALANCE,
    ALL_TRANSACTIONS,
}

@Immutable
data class Transaction(
    val isTransactionSuccessful: Boolean,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val transactionType: TransactionType,
)