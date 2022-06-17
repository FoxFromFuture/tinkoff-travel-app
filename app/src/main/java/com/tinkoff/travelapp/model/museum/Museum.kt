package com.tinkoff.travelapp.model.museum

data class Museum(
    val id: Int,
    val name: String,
    val type: String,
    val coordinateX: Double,
    val coordinateY: Double,
    val description: String,
    val siteLink: String,
    val openTime: String,
    val closeTime: String,
    val price: Int,
    val discountForChildren: Boolean
)