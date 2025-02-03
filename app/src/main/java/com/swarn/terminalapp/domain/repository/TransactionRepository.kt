package com.swarn.terminalapp.domain.repository

import com.swarn.terminalapp.data.local.TransactionLocalDataSource
import com.swarn.terminalapp.data.local.Transactions
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.model.TransactionType
import com.swarn.terminalapp.util.convertTimestampToFormattedDateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.math.BigDecimal

class TransactionRepository(
    private val transactionLocalDataSource: TransactionLocalDataSource,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : Repository(dispatcher) {

    suspend fun deposit(amount: BigDecimal): Transaction {
        return getResult {
            val transaction =
                Transactions(amount = amount, transactionType = TransactionType.CREDIT)
            transactionLocalDataSource.insertTransaction(transaction)
            val totalBalance = transactionLocalDataSource.getTotalBalance()
            Transaction(
                isTransactionSuccessful = true,
                totalBalance = totalBalance,
                transactionType = TransactionType.CREDIT
            )
        }
    }

    suspend fun withdraw(amount: BigDecimal): Transaction {
        return getResult {
            val canWithdraw = canWithdraw(amount)
            if (canWithdraw) {
                val transaction =
                    Transactions(
                        amount = amount,
                        transactionType = TransactionType.DEBIT
                    )
                transactionLocalDataSource.insertTransaction(transaction)
                val totalBalance = transactionLocalDataSource.getTotalBalance()
                Transaction(
                    isTransactionSuccessful = true,
                    totalBalance = totalBalance,
                    transactionType = TransactionType.DEBIT
                )
            } else {
                val totalBalance = transactionLocalDataSource.getTotalBalance()
                Transaction(
                    isTransactionSuccessful = false,
                    totalBalance = totalBalance,
                    transactionType = TransactionType.DEBIT
                )
            }
        }
    }

    suspend fun getTotalBalance(): Transaction {
        return getResult {
            val totalBalance = transactionLocalDataSource.getTotalBalance()
            Transaction(
                isTransactionSuccessful = true,
                totalBalance = totalBalance,
                transactionType = TransactionType.CHECK_BALANCE
            )
        }
    }

    suspend fun getAllTransactions(): List<TransactionHistory> {
        return getResult {
            val transactions = transactionLocalDataSource.getAllTransactions()
            transactions.map { transaction ->
                TransactionHistory(
                    id = transaction.id,
                    amount = transaction.amount,
                    transactionType = transaction.transactionType,
                    dateAndTime = convertTimestampToFormattedDateTime(transaction.timestamp)
                )
            }
        }
    }

    suspend fun canWithdraw(withdrawAmount: BigDecimal): Boolean {
        val totalBalance = transactionLocalDataSource.getTotalBalance()
        return totalBalance >= withdrawAmount
    }
}
