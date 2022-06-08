package com.tinkoff.travelapp.model.route

data class RouteItem(
    val id: Int = -1,
    val name: String = "",
    val type: String = "",
    val coordinateX: Double = 0.0,
    val coordinateY: Double = 0.0,
    val description: String = "",
    val siteLink: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val price: Int = -1
)