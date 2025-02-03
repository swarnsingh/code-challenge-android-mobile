package com.swarn.terminalapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swarn.terminalapp.presentation.history.TransactionHistoryScreen
import com.swarn.terminalapp.presentation.history.TransactionHistoryViewModel
import com.swarn.terminalapp.presentation.home.MainScreen
import com.swarn.terminalapp.presentation.home.MainViewModel

sealed class AppRoute {
    object Home : AppRoute()
    object History : AppRoute()

    fun route(): String {
        return when (this) {
            Home -> "home"
            History -> "history"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoute.Home.route()) {
        composable(AppRoute.Home.route()) {
            val viewModel = hiltViewModel<MainViewModel>()
            val transactionState by viewModel.transactionStatus.collectAsState()
            MainScreen(
                transactionState = transactionState,
                onDepositClick = { viewModel.deposit(it) },
                onWithdrawClick = { viewModel.withdraw(it) },
                onHistoryClick = { navController.navigate(AppRoute.History.route()) }
            )
        }
        composable(AppRoute.History.route()) {
            val viewModel = hiltViewModel<TransactionHistoryViewModel>()
            val transactionHistoryState by viewModel.transactionHistoryState.collectAsState()
            TransactionHistoryScreen(
                transactionHistoryState = transactionHistoryState,
                onBackClick = { navController.popBackStack() },
                onRetryClick = { viewModel.getAllTransactions() }
            )
        }
    }
}