package com.tinkoff.travelapp.model.bar

data class Bar(
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
    val forAdults: Boolean
)