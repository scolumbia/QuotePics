package com.bignerdranch.android.quotepics.api

import com.bignerdranch.android.quotepics.Quote
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteAPI {

    @GET("/quote")
    suspend fun getQuotes(): List<Quote>

    @GET("/quote/get_pics")
    suspend fun getQuoteImages(@Query("quote_id") quoteId: Int): List<String>


}