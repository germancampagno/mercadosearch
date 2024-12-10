package com.germancampagno.mercadosearch.data.repository

import com.germancampagno.mercadosearch.data.api.MercadoLibreApi
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val api: MercadoLibreApi
) {
    suspend fun getCategories(siteId: String = "MLA") = api.getCategories(siteId)
}
