package com.swarn.terminalapp.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swarn.terminalapp.domain.model.TransactionType
import java.math.BigDecimal

@Entity(tableName = "transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val timestamp: Long = System.currentTimeMillis(),
)