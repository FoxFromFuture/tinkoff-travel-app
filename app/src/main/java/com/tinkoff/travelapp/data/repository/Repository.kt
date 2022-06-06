package com.tinkoff.travelapp.data.repository

import com.tinkoff.travelapp.data.api.RetrofitGetRouteRequest
import com.tinkoff.travelapp.data.api.RetrofitInstance
import com.tinkoff.travelapp.model.bar.Bar
import com.tinkoff.travelapp.model.cafe.Cafe
import com.tinkoff.travelapp.model.museum.Museum
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.street.Street
import retrofit2.Response

class Repository {

//    private val categoriesList = listOf("STREET", "BAR", "MUSEUM", "CAFE")

//    suspend fun getRoute(): Response<Route> {
//        return RetrofitInstance.api.getRoute(
//            RetrofitGetRouteRequest(
//                categories = categoriesList,
//                startTime = "00:00",
//                endTime = "23:59",
//                budget = 50
//            )
//        )
//    }

    suspend fun getRoute(categories: List<String>, startTime: String, endTime: String, budget: Int): Response<Route> {
        return RetrofitInstance.api.getRoute(
            RetrofitGetRouteRequest(categories, startTime, endTime, budget)
        )
    }

    suspend fun getStreet(): Response<Street> {
        return RetrofitInstance.api.getStreet()
    }

    suspend fun getBar(): Response<Bar> {
        return RetrofitInstance.api.getBar(3)
    }

    suspend fun getCafe(): Response<Cafe> {
        return RetrofitInstance.api.getCafe()
    }

    suspend fun getMuseum(): Response<Museum> {
        return RetrofitInstance.api.getMuseum()
    }
}