package com.tinkoff.travelapp.data.api

import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.street.Street
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/route")
    suspend fun getRoute(@Body elementModel: RetrofitGetRouteRequest): Response<Route>

    @GET("/street?id=583")
    suspend fun getStreet(@Header("Authorization") auth: String): Response<Street>
}
