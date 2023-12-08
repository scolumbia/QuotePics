package com.bignerdranch.android.quotepics

import androidx.lifecycle.ViewModel

class QuotesListViewModel : ViewModel() {
    val quotes = mutableListOf<Quote>()

    init {
        for (i in 0 until 100) {
            val quote = Quote(
                quoteId = i,
                text = "Quote number $i",
                author = "Author of quote number $i"
            )

            quotes += quote
        }
    }
}