package com.swarn.terminalapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "balance")
data class Balance(
    @PrimaryKey val id: Int = 1,
    val totalBalance: BigDecimal = BigDecimal.ZERO
)