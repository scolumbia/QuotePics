package com.bignerdranch.android.quotepics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private const val TAG = "QuotesListViewModel"

class QuotesListViewModel : ViewModel() {
    private val quotesRepository = QuoteRepository()

    private val _uiState: MutableStateFlow<QuotesListUiState> = MutableStateFlow(QuotesListUiState())

    val uiState: StateFlow<QuotesListUiState>
        get() = _uiState.asStateFlow()

    val quotes: List<Quote>
        get() = _uiState.value.quotes

    init {
        viewModelScope.launch{
            try {
                viewModelScope.launch {
                    val curQuotes = QuoteRepository().getQuotes()
                    Log.d(TAG, "Got quotes from API: $curQuotes")
                    _uiState.update {
                        it.copy(
                            quotes = curQuotes
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to load moments list from API", ex)
            }
        }
    }
}

data class QuotesListUiState(
    val quotes: List<Quote> = listOf()
)
