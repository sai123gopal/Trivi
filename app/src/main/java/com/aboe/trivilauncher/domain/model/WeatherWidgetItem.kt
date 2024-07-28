package com.aboe.trivilauncher.domain.model

data class WeatherWidgetItem(
    val temperature: String,
    val icon: String,
    val iconResource: Int? = null,
)