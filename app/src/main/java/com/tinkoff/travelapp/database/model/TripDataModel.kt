package com.tinkoff.travelapp.database.model

import com.tinkoff.travelapp.model.route.Route

data class TripDataModel(
    val title: String = "",
    val date: String = "",
    val route: Route = Route()
)