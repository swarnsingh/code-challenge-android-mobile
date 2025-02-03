package com.swarn.terminalapp.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.data.fetchResponse
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _transactionHistoryState =
        MutableStateFlow<ResultState<List<TransactionHistory>>>(ResultState.Loading())
    val transactionHistoryState: StateFlow<ResultState<List<TransactionHistory>>>
        get() = _transactionHistoryState

    init {
        getAllTransactions()
    }

    fun getAllTransactions() {
        viewModelScope.launch {
            _transactionHistoryState.fetchResponse {
                transactionRepository.getAllTransactions()
            }
        }
    }
}
