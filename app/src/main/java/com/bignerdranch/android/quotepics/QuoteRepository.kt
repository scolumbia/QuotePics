package com.bignerdranch.android.quotepics

import com.bignerdranch.android.quotepics.api.QuoteAPI
import com.bignerdranch.android.quotepics.api.QuoteAPIAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.create
import java.io.File


const val ENDPOINT_URL = "http://beemail.page:5000/"

class QuoteRepository {
    private val quoteAPI: QuoteAPI

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val moshiConfig = Moshi.Builder()
            .add(QuoteAPIAdapter())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshiConfig))
            .client(okHttpClient)
            .build()

        quoteAPI = retrofit.create()
    }


    suspend fun getQuotes(): List<Quote> {
        return quoteAPI.getQuotes()
    }

    suspend fun getQuoteImages(quoteId: Int): List<String> {
        return quoteAPI.getQuoteImages(quoteId)
    }

    suspend fun addQuoteImage(id: Int, photoFile: File) {
        val photoFileBase64 = withContext(Dispatchers.IO) {
            android.util.Base64.encodeToString(
                photoFile.readBytes(),
                android.util.Base64.NO_WRAP
            )
        }
        quoteAPI.addQuoteImage(id, photoFileBase64)
    }
}
