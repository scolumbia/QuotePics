package com.bignerdranch.android.quotepics.api

import android.util.Log
import com.bignerdranch.android.quotepics.Quote
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val TAG = "QuoteAPIAdapter"

class QuoteAPIAdapter {

    @FromJson
    fun jsonToId(id: String): Int {
        Log.d(TAG, "jsonToId: $id")
        return id.toInt()
    }

    @FromJson
    fun jsonToText(quote: String): String {
        return quote
    }
}