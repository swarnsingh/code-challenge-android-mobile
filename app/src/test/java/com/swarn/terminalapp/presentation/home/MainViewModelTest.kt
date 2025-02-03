import app.cash.turbine.test
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.data.local.TransactionLocalDataSource
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.model.TransactionType
import com.swarn.terminalapp.domain.repository.TransactionRepository
import com.swarn.terminalapp.presentation.home.MainViewModel
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
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: TransactionRepository
    private val transactionLocalDataSource: TransactionLocalDataSource = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = TransactionRepository(transactionLocalDataSource, testDispatcher)
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTotalBalance should emit success state when repository returns balance`() = runTest {
        // Given
        val amount = 100.0.toBigDecimal()
        val mockTransaction = Transaction(
            isTransactionSuccessful = true,
            totalBalance = amount,
            transactionType = TransactionType.CHECK_BALANCE
        )
        coEvery { transactionLocalDataSource.getTotalBalance() } returns amount

        // When
        viewModel.getTotalBalance()

        // Then
        viewModel.transactionStatus.test {
            val result = awaitItem()
            assert(result is ResultState.Success)
            assertEquals(result.data, mockTransaction)
        }
    }

    @Test
    fun `deposit should emit success state when deposit is successful`() = runTest {
        // Given
        val depositAmount = 50.0
        val totalAmount = 150.0.toBigDecimal()
        val mockTransaction = Transaction(
            isTransactionSuccessful = true,
            totalBalance = totalAmount,
            transactionType = TransactionType.CREDIT
        )
        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalAmount

        // When
        viewModel.deposit(depositAmount)

        // Then
        viewModel.transactionStatus.test {
            val result = awaitItem()
            assert(result is ResultState.Success)
            assertEquals(mockTransaction, result.data)
        }
    }

    @Test
    fun `withdraw should emit success state when withdrawal is successful`() = runTest {
        // Given
        val withdrawAmount = 200.0
        val totalAmount = 400.0.toBigDecimal()
        val mockTransaction = Transaction(
            isTransactionSuccessful = true,
            totalBalance = totalAmount,
            transactionType = TransactionType.DEBIT
        )

        coEvery { transactionLocalDataSource.getTotalBalance() } returns totalAmount

        // When
        viewModel.withdraw(withdrawAmount)

        // Then
        viewModel.transactionStatus.test {
            val result = awaitItem()
            assert(result is ResultState.Success)
            assertEquals((result as ResultState.Success).data, mockTransaction)
        }
    }

    @Test
    fun `withdraw should emit success state with isTransactionSuccessful false when withdrawal is not possible`() =
        runTest {
            // Given
            val withdrawAmount = 200.0
            val totalAmount = 100.0.toBigDecimal()
            val mockTransaction = Transaction(
                isTransactionSuccessful = false,
                totalBalance = totalAmount,
                transactionType = TransactionType.DEBIT
            )

            coEvery { transactionLocalDataSource.getTotalBalance() } returns totalAmount

            // When
            viewModel.withdraw(withdrawAmount)

            // Then
            viewModel.transactionStatus.test {
                val result = awaitItem()
                assert(result is ResultState.Success)
                assertEquals((result as ResultState.Success).data, mockTransaction)
            }
        }
}