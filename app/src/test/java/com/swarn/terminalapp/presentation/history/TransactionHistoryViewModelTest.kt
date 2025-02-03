package com.swarn.terminalapp.presentation.history

import app.cash.turbine.test
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.data.local.TransactionLocalDataSource
import com.swarn.terminalapp.data.local.Transactions
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.model.TransactionType
import com.swarn.terminalapp.domain.repository.TransactionRepository
import com.swarn.terminalapp.util.convertTimestampToFormattedDateTime
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionHistoryViewModelTest {
    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var repository: TransactionRepository
    private val transactionLocalDataSource: TransactionLocalDataSource = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = TransactionRepository(transactionLocalDataSource, testDispatcher)
        viewModel = TransactionHistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getAllTransactions should emit success state when repository returns data`() = runTest {
        // Given
        val transactionHistory = listOf(
            TransactionHistory(
                id = 1,
                amount = 50.0.toBigDecimal(),
                transactionType = TransactionType.CREDIT,
                dateAndTime = convertTimestampToFormattedDateTime(System.currentTimeMillis())
            ),
            TransactionHistory(
                id = 2,
                amount = 100.0.toBigDecimal(),
                transactionType = TransactionType.DEBIT,
                dateAndTime = convertTimestampToFormattedDateTime(System.currentTimeMillis())
            )
        )
        coEvery { transactionLocalDataSource.getAllTransactions() } returns transactionHistory.map {
            Transactions(
                id = it.id,
                amount = it.amount,
                transactionType = it.transactionType,
                timestamp = System.currentTimeMillis()
            )
        }

        // When
        viewModel.getAllTransactions()

        // Then
        viewModel.transactionHistoryState.test {
            val result = awaitItem()
            assert(result is ResultState.Success)
            assertEquals((result as ResultState.Success).data, transactionHistory)
        }
    }

    @Test
    fun `getAllTransactions should emit error state when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Failed to fetch transactions")
        coEvery { transactionLocalDataSource.getAllTransactions() } throws exception

        // When
        viewModel.getAllTransactions()

        // Then
        viewModel.transactionHistoryState.test {
            val result = awaitItem()
            assert(result is ResultState.Error)
            assertEquals((result as ResultState.Error).throwable.message, exception.message)
        }
    }
}