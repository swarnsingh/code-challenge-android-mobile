package com.swarn.terminalapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import java.math.BigDecimal

@Dao
interface TransactionsDao {

    @Insert
    suspend fun insertTransaction(transaction: Transactions)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT totalBalance FROM balance WHERE id = 1")
    suspend fun getTotalBalance(): BigDecimal

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transactions>
}
