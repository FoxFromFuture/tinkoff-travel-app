package com.tinkoff.travelapp.data.api

data class RetrofitGetRouteRequest(
    val categories: List<String>,
    val startTime: String,
    val endTime: String,
    val budget: Int
)