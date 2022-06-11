package com.tinkoff.travelapp.data.repository

import com.tinkoff.travelapp.data.api.RetrofitGetRouteRequest
import com.tinkoff.travelapp.data.api.RetrofitInstance
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.street.Street
import retrofit2.Response

class Repository {

    suspend fun getRoute(
        categories: List<String>,
        startTime: String,
        endTime: String,
        budget: Int
    ): Response<Route> {
        return RetrofitInstance.api.getRoute(
            RetrofitGetRouteRequest(categories, startTime, endTime, budget)
        )
    }

    suspend fun getStreet(auth: String): Response<List<Street>> {
        return RetrofitInstance.api.getStreet(auth)
    }
}
