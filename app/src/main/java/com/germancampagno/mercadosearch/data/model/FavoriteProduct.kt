package com.germancampagno.mercadosearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteProduct(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val thumbnail: String
)