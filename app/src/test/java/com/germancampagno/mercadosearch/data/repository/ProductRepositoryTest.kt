package com.germancampagno.mercadosearch.data.repository

import com.germancampagno.mercadosearch.data.api.MercadoLibreApi
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.model.ProductDescriptionResponse
import com.germancampagno.mercadosearch.data.model.SearchResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ProductRepositoryTest {

    @Mock
    private lateinit var api: MercadoLibreApi

    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ProductRepository(api)
    }

    @Test
    fun `searchProducts should return SearchResponse`() = runBlocking {
        val query = "test"
        val offset = 0
        val expectedResponse = SearchResponse(
            results = listOf(getProduct("1", "Test Product")),
            paging = SearchResponse.Paging(1)
        )
        `when`(api.searchProductsByCategory("MLA", "", query.trim(), offset)).thenReturn(expectedResponse)

        val response = repository.searchProducts(query, offset)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `searchProductsByCategory should return SearchResponse`() = runBlocking {
        val query = "test"
        val categoryId = "categoryId"
        val offset = 0
        val expectedResponse = SearchResponse(
            results = listOf(getProduct("1", "Test Product")),
            paging = SearchResponse.Paging(1)
        )
        `when`(api.searchProductsByCategory("MLA", categoryId, query.trim(), offset)).thenReturn(expectedResponse)

        val response = repository.searchProductsByCategory(query, categoryId, offset)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `getProductDetails should return Product`() = runBlocking {
        val itemId = "1"
        val expectedProduct = getProduct(itemId, "Test Product")
        `when`(api.getProductDetails(itemId)).thenReturn(expectedProduct)

        val product = repository.getProductDetails(itemId)

        assertEquals(expectedProduct, product)
    }

    @Test
    fun `getProductDescription should return Product Description`() = runBlocking {
        val itemId = "1"
        val expectedDescription = ProductDescriptionResponse("Test Description")
        `when`(api.getProductDescription(itemId)).thenReturn(expectedDescription)

        val description = repository.getProductDescription(itemId)

        assertEquals(expectedDescription, description)
    }

    private fun getProduct(id: String, title: String): Product {
        return Product(
            id,
            title,
            100.0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}
