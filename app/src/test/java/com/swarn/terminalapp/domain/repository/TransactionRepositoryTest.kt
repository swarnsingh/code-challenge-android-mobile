package com.swarn.terminalapp.domain.repository

import com.swarn.terminalapp.data.local.TransactionLocalDataSource
import com.swarn.terminalapp.data.local.Transactions
import com.swarn.terminalapp.domain.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal


@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryTest {

    private lateinit var repository: TransactionRepository
    private val transactionLocalDataSource: TransactionLocalDataSource = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        repository = TransactionRepository(transactionLocalDataSource, testDispatcher)
    }

    @Test
    fun `deposit should insert transaction and return updated balance`() = runTest {
        // Given
        val depositAmount = 100.0.toBigDecimal()
        val totalBalance = 500.0.toBigDecimal()
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

        // When
        val result = repository.deposit(depositAmount)

        // Then
        coVerify { transactionLocalDataSource.insertTransaction(any()) }
        assert(result.isTransactionSuccessful)
        assertEquals(TransactionType.CREDIT, result.transactionType)
        assertEquals(totalBalance, result.totalBalance)
    }

    @Test
    fun `withdraw should insert transaction and return updated balance if sufficient balance`() =
        runTest {
            // Given
            val withdrawAmount = 200.0.toBigDecimal()
            val totalBalance = 500.0.toBigDecimal()
            coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

            // When
            val result = repository.withdraw(withdrawAmount)

            // Then
            coVerify { transactionLocalDataSource.insertTransaction(any()) }
            assert(result.isTransactionSuccessful)
            assertEquals(TransactionType.DEBIT, result.transactionType)
            assertEquals(totalBalance, result.totalBalance)
        }

    @Test
    fun `withdraw should return unsuccessful transaction if insufficient balance`() = runTest {
        // Given
        val withdrawAmount = 600.0.toBigDecimal()
        val totalBalance = 500.0.toBigDecimal()
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

        // When
        val result = repository.withdraw(withdrawAmount)

        // Then
        coVerify(exactly = 0) { transactionLocalDataSource.insertTransaction(any()) }
        assert(!result.isTransactionSuccessful)
        assertEquals(TransactionType.DEBIT, result.transactionType)
        assertEquals(totalBalance, result.totalBalance)
    }

    @Test
    fun `getTotalBalance should return the current balance`() = runTest {
        // Given
        val totalBalance = 300.0.toBigDecimal()
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

        // When
        val result = repository.getTotalBalance()

        // Then
        assert(result.isTransactionSuccessful)
        assertEquals(TransactionType.CHECK_BALANCE, result.transactionType)
        assertEquals(totalBalance, result.totalBalance)
    }

    @Test
    fun `getAllTransactions should return list of transaction histories`() = runTest {
        // Given
        val transactions = listOf(
            Transactions(
                id = 1,
                amount = BigDecimal(100.0),
                transactionType = TransactionType.CREDIT,
                timestamp = System.currentTimeMillis()
            ),
            Transactions(
                id = 2,
                amount = BigDecimal(200.0),
                transactionType = TransactionType.DEBIT,
                timestamp = System.currentTimeMillis()
            )
        )
        coEvery { transactionLocalDataSource.getAllTransactions() } returns transactions

        // When
        val result = repository.getAllTransactions()

        // Then
        assert(result.isNotEmpty())
        assertEquals(transactions.size, result.size)
        assertEquals(transactions[0].id, result[0].id)
        assertEquals(transactions[0].amount, result[0].amount)
        assertEquals(transactions[0].transactionType, result[0].transactionType)
    }

    @Test
    fun `canWithdraw should return true if balance is sufficient`() = runTest {
        // Given
        val withdrawAmount = 200.0.toBigDecimal()
        val totalBalance = 500.0.toBigDecimal()
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

        // When
        val result = repository.canWithdraw(withdrawAmount)

        // Then
        assert(result)
    }

    @Test
    fun `canWithdraw should return false if balance is insufficient`() = runTest {
        // Given
        val withdrawAmount = 600.0.toBigDecimal()
        val totalBalance = 500.0.toBigDecimal()
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalBalance

        // When
        val result = repository.canWithdraw(withdrawAmount)

        // Then
        assert(!result)
    }
}
