package com.germancampagno.mercadosearch.data.repository

import com.germancampagno.mercadosearch.data.api.MercadoLibreApi
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.model.SearchResponse
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val api: MercadoLibreApi
) {
    suspend fun searchProducts(query: String, offset: Int = 0): SearchResponse {
        return api.searchProductsByCategory("MLA", "", query.trim(), offset)
    }

    suspend fun searchProductsByCategory(
        query: String,
        categoryId: String,
        offset: Int = 0
    ): SearchResponse {
        return api.searchProductsByCategory("MLA", categoryId, query.trim(), offset)
    }

    suspend fun getProductDetails(itemId: String) = api.getProductDetails(itemId)
    suspend fun getProductDescription(itemId: String) = api.getProductDescription(itemId)
}