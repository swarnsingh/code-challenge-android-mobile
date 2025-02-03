package com.swarn.terminalapp.data.local

import com.swarn.terminalapp.domain.model.TransactionType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TransactionLocalDataSourceTest {

    private val transactionsDao = mockk<TransactionsDao>()
    private val dataSource = TransactionLocalDataSource(transactionsDao)

    @Test
    fun `getTotalBalance should return correct balance`() = runTest {
        val expectedBalance = BigDecimal(1000.0)
        coEvery { transactionsDao.getTotalBalance() } returns expectedBalance

        val actualBalance = dataSource.getTotalBalance()
        assertEquals(expectedBalance, actualBalance)
    }

    @Test
    fun `getAllTransactions should return all transactions`() = runTest {
        val expectedTransactions = listOf(
            Transactions(
                id = 1,
                amount = BigDecimal(100.0),
                transactionType = TransactionType.DEBIT,
                timestamp = System.currentTimeMillis()
            ),
            Transactions(
                id = 2,
                amount = BigDecimal(50.0),
                transactionType = TransactionType.CREDIT,
                timestamp = System.currentTimeMillis()
            )
        )
        coEvery { transactionsDao.getAllTransactions() } returns expectedTransactions

        val actualTransactions = dataSource.getAllTransactions()
        assertEquals(expectedTransactions, actualTransactions)
    }
}
