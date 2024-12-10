package com.germancampagno.mercadosearch.ui.util

sealed class Currency {
    data object ARS : Currency()

    data object USD : Currency()

    companion object {
        fun from(currencyCode: String?): Currency {
            return when (currencyCode) {
                "ARS" -> ARS
                "USD" -> USD
                else -> ARS
            }
        }
    }
}