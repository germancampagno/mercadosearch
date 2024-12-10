package com.germancampagno.mercadosearch.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    @SerializedName("currency_id") val currency: String?,
    val thumbnail: String?,
    val description: String?,
    val permalink: String?,
    val condition: String?,
    val pictures: List<Picture>?,
    val address: Address?,
    @SerializedName("seller_address") val sellerAddress: SellerAddress?,
    val formattedPrice: String?,
    val formattedAddress: String?
) {
    data class Picture(
        val id: String,
        val url: String,
    )

    data class Address(
        @SerializedName("state_name") val state: String?,
        @SerializedName("city_name") val city: String?
    )

    data class SellerAddress(
        val city: City,
        val state: State
    ) {
        data class City(
            val name: String
        )

        data class State(
            val name: String
        )
    }

    sealed class Condition(val value: String) {
        data object New : Condition("new")
        data object Used : Condition("used")

        companion object {
            fun from(value: String?): Condition {
                return when (value) {
                    New.value -> New
                    Used.value -> Used
                    else -> New
                }
            }
        }
    }
}