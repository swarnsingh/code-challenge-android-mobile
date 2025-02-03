package com.swarn.terminalapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.data.fetchResponse
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _transactionStatus =
        MutableStateFlow<ResultState<Transaction>>(ResultState.Loading())
    val transactionStatus: StateFlow<ResultState<Transaction>>
        get() = _transactionStatus

    init {
        getTotalBalance()
    }

    fun deposit(amount: Double) {
        viewModelScope.launch {
            _transactionStatus.fetchResponse {
                transactionRepository.deposit(amount.toBigDecimal())
            }
        }
    }

    fun withdraw(amount: Double) {
        viewModelScope.launch {
            _transactionStatus.fetchResponse {
                transactionRepository.withdraw(amount.toBigDecimal())
            }
        }
    }

    fun getTotalBalance() {
        viewModelScope.launch {
            _transactionStatus.fetchResponse {
                transactionRepository.getTotalBalance()
            }
        }
    }
}
