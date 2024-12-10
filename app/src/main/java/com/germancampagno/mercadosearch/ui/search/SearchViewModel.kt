package com.germancampagno.mercadosearch.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.ErrorMessage
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.repository.ProductRepository
import com.germancampagno.mercadosearch.ui.navigation.Screen
import com.germancampagno.mercadosearch.ui.util.ProductFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState
    private var hasCategory = false

    init {
        val categoryId = savedStateHandle.get<String>(Screen.SearchCategory.categoryIdArg)
        if (categoryId != null) {
            hasCategory = true
            setupWithCategory(categoryId)
        }
    }

    private fun setupWithCategory(categoryId: String?) {
        _uiState.update {
            it.copy(
                categoryId = categoryId,
            )
        }.also {
            searchProducts(_uiState.value.query)
        }

    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val results = if (hasCategory) {
                    repository.searchProductsByCategory(
                        query,
                        _uiState.value.categoryId!!,
                        offset = 0
                    )
                } else {
                    repository.searchProducts(query, offset = 0)
                }
                val formattedResults = formatResults(results)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        searchResults = formattedResults,
                        currentOffset = formattedResults.size
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = ErrorMessage.Exception(e.message.toString())
                    )
                }
            }
        }
    }

    private fun formatResults(results: List<Product>): List<Product> {
        val formattedResults = results.map { product ->
            product.copy(
                formattedPrice = ProductFormatter.formatCurrency(product),
                formattedAddress = ProductFormatter.formatAddress(product)
            )
        }
        return formattedResults
    }

    fun loadMoreProducts() {
        if (_uiState.value.isLoadingMore || _uiState.value.searchResults.size >= _uiState.value.totalAvailableItems) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            try {
                val newResults = if (hasCategory) {
                    repository.searchProductsByCategory(
                        query = _uiState.value.query,
                        categoryId = _uiState.value.categoryId!!,
                        offset = _uiState.value.currentOffset
                    )
                } else {
                    repository.searchProducts(
                        query = _uiState.value.query,
                        offset = _uiState.value.currentOffset
                    )
                }
                _uiState.update { state ->
                    val formattedResults = formatResults(newResults)
                    state.copy(
                        searchResults = state.searchResults + formattedResults,
                        currentOffset = state.searchResults.size + formattedResults.size,
                        isLoadingMore = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    // TODO - Refactor this so it doesnt repeat in viewmodels
    fun getConditionResId(condition: Product.Condition): Int {
        return when (condition) {
            Product.Condition.New -> R.string.product_condition_new
            Product.Condition.Used -> R.string.product_condition_used
        }
    }
}

data class SearchUiState(
    val query: String = "",
    val categoryId: String? = null,
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false, // New field for pagination
    val errorMessage: ErrorMessage? = null,
    val currentOffset: Int = 0, // Current offset for pagination
    val totalAvailableItems: Int = Int.MAX_VALUE // Total items available (from API response)
)