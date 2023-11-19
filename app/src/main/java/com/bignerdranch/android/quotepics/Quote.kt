package com.bignerdranch.android.quotepics

import java.util.Date
//import java.util.UUID

data class Quote(var text: String, var author: String, var dt: Date = Date())
//    data class Quote(
////        val id: UUID,
//        var text: String,
//        var author: String,
//        var dt: Date =Date()
//    )
