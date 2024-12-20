package com.germancampagno.mercadosearch.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.germancampagno.mercadosearch.data.model.ErrorMessage
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.model.SearchResponse
import com.germancampagno.mercadosearch.data.repository.ProductRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var repository: ProductRepository

    private lateinit var viewModel: SearchViewModel

    companion object {
        const val PRODUCT_CONDITION_NEW_RES_ID = 2131427409
        const val PRODUCT_CONDITION_USED_RES_ID = 2131427410
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `searchProducts should update uiState with searchResults on success with categoryId`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to "testCategoryId"))
        viewModel = SearchViewModel(repository, savedStateHandle)

        val products = listOf(getProduct("1", "Product 1"), getProduct("2", "Product 2"))
        val response = SearchResponse(products, SearchResponse.Paging(2))
        `when`(repository.searchProductsByCategory("query", "testCategoryId", 0)).thenReturn(response)
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        val resultProducts = uiState.searchResults
        assertEquals(resultProducts[0].id, "1")
        assertEquals(resultProducts[0].title, "Product 1")
        assertEquals(resultProducts[1].id, "2")
        assertEquals(resultProducts[1].title, "Product 2")
        assertNull(uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `searchProducts should update uiState with errorMessage on failure with categoryId`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to "testCategoryId"))
        viewModel = SearchViewModel(repository, savedStateHandle)

        val exceptionMessage = "Network error"
        `when`(repository.searchProductsByCategory("query", "testCategoryId", 0)).thenThrow(RuntimeException(exceptionMessage))
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState.searchResults.isEmpty())
        assertEquals(ErrorMessage.Exception(exceptionMessage), uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `searchProducts should update uiState with searchResults on success without categoryId`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = SearchViewModel(repository, savedStateHandle)

        val products = listOf(getProduct("1", "Product 1"), getProduct("2", "Product 2"))
        val response = SearchResponse(products, SearchResponse.Paging(2))
        `when`(repository.searchProducts("query", 0)).thenReturn(response)
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        val resultProducts = uiState.searchResults
        assertEquals(resultProducts[0].id, "1")
        assertEquals(resultProducts[0].title, "Product 1")
        assertEquals(resultProducts[1].id, "2")
        assertEquals(resultProducts[1].title, "Product 2")
        assertNull(uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `searchProducts should update uiState with errorMessage on failure without categoryId`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = SearchViewModel(repository, savedStateHandle)

        val exceptionMessage = "Network error"
        `when`(repository.searchProducts("query", 0)).thenThrow(RuntimeException(exceptionMessage))
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState.searchResults.isEmpty())
        assertEquals(ErrorMessage.Exception(exceptionMessage), uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `onQueryChange should update uiState with new query`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = SearchViewModel(repository, savedStateHandle)

        val newQuery = "new query"
        viewModel.onQueryChange(newQuery)

        val uiState = viewModel.uiState.value
        assertEquals(newQuery, uiState.query)
    }

    @Test
    fun `loadMoreProducts should update uiState with more products on success`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to "testCategoryId"))
        viewModel = SearchViewModel(repository, savedStateHandle)

        val initialProducts = listOf(getProduct("1", "Product 1"))
        val initialResponse = SearchResponse(initialProducts, SearchResponse.Paging(2))
        `when`(repository.searchProductsByCategory("query", "testCategoryId", 0)).thenReturn(initialResponse)
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val moreProducts = listOf(getProduct("2", "Product 2"))
        val moreResponse = SearchResponse(moreProducts, SearchResponse.Paging(2))
        `when`(repository.searchProductsByCategory("", "testCategoryId", 1)).thenReturn(moreResponse)
        viewModel.loadMoreProducts()

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        val resultProducts = uiState.searchResults
        assertEquals(2, resultProducts.size)
        assertEquals("1", resultProducts[0].id)
        assertEquals("Product 1", resultProducts[0].title)
        assertEquals("2", resultProducts[1].id)
        assertEquals("Product 2", resultProducts[1].title)
        assertFalse(uiState.isLoadingMore)
    }

    @Test
    fun `loadMoreProducts should update uiState with more products on success without categoryId`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = SearchViewModel(repository, savedStateHandle)

        val initialProducts = listOf(getProduct("1", "Product 1"))
        val initialResponse = SearchResponse(initialProducts, SearchResponse.Paging(2))
        `when`(repository.searchProducts("query", 0)).thenReturn(initialResponse)
        viewModel.searchProducts("query")

        advanceUntilIdle()

        val moreProducts = listOf(getProduct("2", "Product 2"))
        val moreResponse = SearchResponse(moreProducts, SearchResponse.Paging(2))
        `when`(repository.searchProducts("", 1)).thenReturn(moreResponse)
        viewModel.loadMoreProducts()

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        val resultProducts = uiState.searchResults
        assertEquals(2, resultProducts.size)
        assertEquals("1", resultProducts[0].id)
        assertEquals("Product 1", resultProducts[0].title)
        assertEquals("2", resultProducts[1].id)
        assertEquals("Product 2", resultProducts[1].title)
        assertFalse(uiState.isLoadingMore)
    }

    @Test
    fun `getConditionResId should return correct resource id`() {
        val savedStateHandle = SavedStateHandle()
        viewModel = SearchViewModel(repository, savedStateHandle)

        val productConditionNewResId = PRODUCT_CONDITION_NEW_RES_ID
        val productConditionUsedResId = PRODUCT_CONDITION_USED_RES_ID

        val newConditionResId = viewModel.getConditionResId(Product.Condition.New)
        val usedConditionResId = viewModel.getConditionResId(Product.Condition.Used)

        assertEquals(productConditionNewResId, newConditionResId)
        assertEquals(productConditionUsedResId, usedConditionResId)
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
