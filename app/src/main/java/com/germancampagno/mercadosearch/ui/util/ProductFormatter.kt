package com.germancampagno.mercadosearch.ui.util

import com.germancampagno.mercadosearch.data.model.Product
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object ProductFormatter {

    fun formatCurrency(productDetails: Product): String {
        val currency = Currency.from(productDetails.currency)
        val formatter = DecimalFormat("#,###.##")
        formatter.decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
        }
        val formattedAmount = formatter.format(productDetails.price)
        val formattedCurrency = when (currency) {
            Currency.ARS -> "$"
            Currency.USD -> "US$"
        }
        return "$formattedCurrency $formattedAmount"
    }

    fun formatAddress(product: Product): String {
        return product.address?.let {
            "${it.city}, ${it.state}"
        } ?: run {
            val sellerAddress = product.sellerAddress
            "${sellerAddress?.city?.name}, ${sellerAddress?.state?.name}"
        }
    }

}
