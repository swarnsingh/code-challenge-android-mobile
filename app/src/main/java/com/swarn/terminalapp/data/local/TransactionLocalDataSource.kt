package com.swarn.terminalapp.data.local

import java.math.BigDecimal


class TransactionLocalDataSource(private val transactionsDao: TransactionsDao) {

    suspend fun insertTransaction(transaction: Transactions) {
        transactionsDao.insertTransaction(transaction)
    }

    suspend fun getTotalBalance(): BigDecimal {
        return transactionsDao.getTotalBalance()
    }

    suspend fun getAllTransactions(): List<Transactions> {
        return transactionsDao.getAllTransactions()
    }
}
