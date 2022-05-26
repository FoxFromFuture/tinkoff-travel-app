package com.tinkoff.travelapp.data.api

import com.tinkoff.travelapp.model.bar.Bar
import com.tinkoff.travelapp.model.cafe.Cafe
import com.tinkoff.travelapp.model.museum.Museum
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.street.Street
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/route")
    suspend fun getRoute(): Response<Route>

    @GET("/street?id=3")
    suspend fun getStreet(): Response<Street>

    @GET("/bar")
    suspend fun getBar(@Query("id") barId: Int): Response<Bar>

    @GET("/cafe")
    suspend fun getCafe(): Response<Cafe>

    @GET("/museum")
    suspend fun getMuseum(): Response<Museum>
}