package com.bignerdranch.android.quotepics.api

import com.bignerdranch.android.quotepics.Quote
import retrofit2.http.GET

interface QuoteAPI {

    @GET("/quote")
    suspend fun getQuotes(): List<Quote>


}