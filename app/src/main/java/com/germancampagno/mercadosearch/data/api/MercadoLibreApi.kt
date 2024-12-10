package com.germancampagno.mercadosearch.data.api

import com.germancampagno.mercadosearch.data.model.Category
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.model.ProductDescriptionResponse
import com.germancampagno.mercadosearch.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreApi {
    @GET("sites/{site_id}/categories")
    suspend fun getCategories(@Path("site_id") siteId: String): List<Category>

    @GET("sites/{site_id}/search")
    suspend fun searchProductsByCategory(
        @Path("site_id") siteId: String,
        @Query("category") categoryId: String,
        @Query("q") query: String,
        @Query("offset") offset: Int = 0
    ): SearchResponse

    @GET("items/{item_id}")
    suspend fun getProductDetails(@Path("item_id") itemId: String): Product

    @GET("items/{item_id}/description")
    suspend fun getProductDescription(@Path("item_id") itemId: String): ProductDescriptionResponse
}
