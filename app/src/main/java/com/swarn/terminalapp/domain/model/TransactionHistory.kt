package com.swarn.terminalapp.domain.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

@Immutable
data class TransactionHistory(
    val id: Int,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val dateAndTime: String,
)
