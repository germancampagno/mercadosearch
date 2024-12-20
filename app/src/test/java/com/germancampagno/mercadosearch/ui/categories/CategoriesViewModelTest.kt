package com.germancampagno.mercadosearch.ui.categories


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.germancampagno.mercadosearch.data.model.Category
import com.germancampagno.mercadosearch.data.model.ErrorMessage
import com.germancampagno.mercadosearch.data.repository.CategoryRepository
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
class CategoriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var repository: CategoryRepository

    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CategoriesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `fetchCategories should update uiState with categories on success`() = testScope.runTest {
        val categories = listOf(Category("1", "Category 1"), Category("2", "Category 2"))
        `when`(repository.getCategories()).thenReturn(categories)
        viewModel.fetchCategories()

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(categories, uiState.categories)
        assertNull(uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `fetchCategories should update uiState with errorMessage on failure`() = testScope.runTest {
        val exceptionMessage = "Network error"
        `when`(repository.getCategories()).thenThrow(RuntimeException(exceptionMessage))
        viewModel.fetchCategories()

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState.categories.isEmpty())
        assertEquals(ErrorMessage.Exception(exceptionMessage), uiState.errorMessage)
        assertFalse(uiState.isLoading)
    }
}
