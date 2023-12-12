package com.bignerdranch.android.quotepics.api

import com.bignerdranch.android.quotepics.Quote
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File

interface QuoteAPI {

    @GET("/quote")
    suspend fun getQuotes(): List<Quote>

    @GET("/quote/get_pics")
    suspend fun getQuoteImages(@Query("quote_id") quoteId: Int): List<String>

    @POST("/quote/add_pic_base64")
    suspend fun addQuoteImage(@Query("quote_id") id: Int, @Body photoFile: String)


}