package com.swarn.terminalapp.presentation.history

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.model.TransactionType
import com.swarn.terminalapp.presentation.home.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TransactionHistoryScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testLoadingStateIsDisplayed() {
        composeTestRule.setContent {
            val transactionHistoryState = MutableStateFlow<ResultState<List<TransactionHistory>>>(
                ResultState.Loading()
            )
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState.collectAsState().value,
                onBackClick = {},
                onRetryClick = {}
            )
        }

        composeTestRule.onNodeWithTag("LoadingOverlay").assertIsDisplayed()
    }

    @Test
    fun testTransactionHistoryListIsDisplayedOnSuccess() {
        val mockTransactionHistory = listOf(
            TransactionHistory(
                id = 1,
                amount = 100.0.toBigDecimal(),
                transactionType = TransactionType.CREDIT,
                dateAndTime = "2025-01-01 10:00:00"
            ),
            TransactionHistory(
                id = 2,
                amount = 50.0.toBigDecimal(),
                transactionType = TransactionType.DEBIT,
                dateAndTime = "2025-01-02 11:00:00"
            )
        )

        composeTestRule.setContent {
            val transactionHistoryState = MutableStateFlow<ResultState<List<TransactionHistory>>>(
                ResultState.Success(mockTransactionHistory)
            )
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState.collectAsState().value,
                onBackClick = {},
                onRetryClick = {}
            )
        }

        composeTestRule.onNodeWithText("100.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("50.0").assertIsDisplayed()
    }

    @Test
    fun testErrorStateIsDisplayed() {
        composeTestRule.setContent {
            val transactionHistoryState = MutableStateFlow<ResultState<List<TransactionHistory>>>(
                ResultState.Error(throwable = Throwable("Test Error"))
            )
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState.collectAsState().value,
                onBackClick = {},
                onRetryClick = {}
            )
        }

        composeTestRule.onNodeWithText("Test Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun testBackButtonClick() {
        var isBackClicked = false

        composeTestRule.setContent {
            val transactionHistoryState = MutableStateFlow<ResultState<List<TransactionHistory>>>(
                ResultState.Success(emptyList())
            )
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState.collectAsState().value,
                onBackClick = { isBackClicked = true },
                onRetryClick = {}
            )
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("BackButton", useUnmergedTree = true).performClick()
        assertTrue(isBackClicked)
    }

    @Test
    fun testRetryButtonClick() {
        var isRetryClicked = false

        composeTestRule.setContent {
            val transactionHistoryState = MutableStateFlow<ResultState<List<TransactionHistory>>>(
                ResultState.Error(throwable = Throwable("Test Error"))
            )
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState.collectAsState().value,
                onBackClick = {},
                onRetryClick = { isRetryClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(isRetryClicked)
    }
}
