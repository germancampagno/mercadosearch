package com.germancampagno.mercadosearch.data.repository

import com.germancampagno.mercadosearch.data.api.MercadoLibreApi
import com.germancampagno.mercadosearch.data.model.Category
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CategoryRepositoryTest {

    @Mock
    private lateinit var api: MercadoLibreApi

    private lateinit var repository: CategoryRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = CategoryRepository(api)
    }

    @Test
    fun `getCategories should return list of categories`() = runBlocking {
        val expectedCategories = listOf(
            Category(id = "MLA5725", name = "Accesorios para Vehículos"),
            Category(id = "MLA1512", name = "Agro")
        )
        `when`(api.getCategories("MLA")).thenReturn(expectedCategories)

        val categories = repository.getCategories("MLA")

        assertEquals(expectedCategories, categories)
    }
}
