package com.germancampagno.mercadosearch.data.model

import com.google.gson.annotations.SerializedName

data class ProductDescriptionResponse(@SerializedName("plain_text") val description: String?)