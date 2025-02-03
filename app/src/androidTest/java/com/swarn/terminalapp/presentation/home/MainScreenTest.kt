package com.swarn.terminalapp.presentation.home

import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swarn.terminalapp.presentation.home.MainActivity
import com.swarn.terminalapp.presentation.home.MainScreen
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.model.TransactionType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testDepositButtonEnabledOnInput() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        composeTestRule.setContent {
            val transactionState = MutableStateFlow<ResultState<Transaction>>(
                ResultState.Success(
                    Transaction(
                        true,
                        transactionType = TransactionType.CHECK_BALANCE,
                        totalBalance = 0.0.toBigDecimal()
                    )
                )
            )
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = { mockViewModel.deposit(it) },
                onWithdrawClick = {},
                onHistoryClick = {}
            )
        }
        composeTestRule.onNodeWithTag("DepositButton").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("AmountInput").performTextInput("100")
        composeTestRule.onNodeWithTag("DepositButton").assertIsEnabled()
    }

    @Test
    fun testDepositClickTriggersDepositAction() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        composeTestRule.setContent {
            val transactionState = MutableStateFlow<ResultState<Transaction>>(
                ResultState.Success(
                    Transaction(
                        true,
                        transactionType = TransactionType.CREDIT
                    )
                )
            )
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = { mockViewModel.deposit(it) },
                onWithdrawClick = {},
                onHistoryClick = {}
            )
        }

        composeTestRule.onNodeWithTag("AmountInput").performTextInput("150")
        composeTestRule.onNodeWithTag("DepositButton").performClick()

        verify { mockViewModel.deposit(150.0) }
    }

    @Test
    fun testWithdrawClickTriggersWithdrawAction() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        composeTestRule.setContent {
            val transactionState = MutableStateFlow<ResultState<Transaction>>(
                ResultState.Success(
                    Transaction(
                        true,
                        transactionType = TransactionType.DEBIT
                    )
                )
            )
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = {},
                onWithdrawClick = { mockViewModel.withdraw(it) },
                onHistoryClick = {}
            )
        }

        composeTestRule.onNodeWithTag("AmountInput").performTextInput("50")
        composeTestRule.onNodeWithTag("WithdrawButton").performClick()

        verify { mockViewModel.withdraw(50.0) }
    }

    @Test
    fun testHistoryButtonClickTriggersNavigation() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    val transactionState = MutableStateFlow<ResultState<Transaction>>(
                        ResultState.Success(
                            Transaction(
                                true,
                                transactionType = TransactionType.CHECK_BALANCE
                            )
                        )
                    )
                    MainScreen(
                        transactionState = transactionState.collectAsState().value,
                        onDepositClick = {},
                        onWithdrawClick = {},
                        onHistoryClick = { navController.navigate("history") }
                    )
                }
                composable("history") {
                    Text("History Screen")
                }
            }
        }

        composeTestRule.onNodeWithTag("HistoryButton").performClick()
        composeTestRule.onNodeWithText("History Screen").assertIsDisplayed()
    }

    @Test
    fun testTotalBalanceIsDisplayed() {
        val transactionState = MutableStateFlow<ResultState<Transaction>>(
            ResultState.Success(
                Transaction(
                    true,
                    transactionType = TransactionType.CHECK_BALANCE,
                    totalBalance = 500.0.toBigDecimal()
                )
            )
        )
        composeTestRule.setContent {
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = {},
                onWithdrawClick = {},
                onHistoryClick = {}
            )
        }
        composeTestRule.onNodeWithText("500.0").assertIsDisplayed()
    }

    @Test
    fun testSnackbarIsShownOnSuccess() {
        val transactionState = MutableStateFlow<ResultState<Transaction>>(
            ResultState.Success(
                Transaction(
                    true,
                    transactionType = TransactionType.CREDIT
                )
            )
        )
        composeTestRule.setContent {
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = {},
                onWithdrawClick = {},
                onHistoryClick = {}
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Amount credited successfully").assertIsDisplayed()
    }

    @Test
    fun testSnackbarIsShownOnError() {
        val transactionState =
            MutableStateFlow<ResultState<Transaction>>(ResultState.Error(throwable = Throwable("Test Error")))
        composeTestRule.setContent {
            MainScreen(
                transactionState = transactionState.collectAsState().value,
                onDepositClick = {},
                onWithdrawClick = {},
                onHistoryClick = {}
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Error: Test Error").assertIsDisplayed()
    }
}
