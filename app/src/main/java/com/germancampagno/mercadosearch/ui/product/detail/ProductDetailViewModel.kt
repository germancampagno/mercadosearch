package com.germancampagno.mercadosearch.ui.product.detail

import androidx.annotation.VisibleForTesting
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
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    init {
        val productId = savedStateHandle.get<String>(Screen.ProductDetails.productIdArg)
        fetchProductDetails(productId)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchProductDetails(productId: String?) {
        viewModelScope.launch {
            if (productId != null) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val productDetails = repository.getProductDetails(productId)
                    val productDescription = repository.getProductDescription(productId)
                    val formattedCurrency = ProductFormatter.formatCurrency(productDetails)
                    val formattedAddress = ProductFormatter.formatAddress(productDetails)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productDetails = productDetails.copy(
                                description = productDescription.description,
                                formattedPrice = formattedCurrency,
                                formattedAddress = formattedAddress
                            )
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = ErrorMessage.Exception(e.message.toString())
                        )
                    }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = ErrorMessage.InvalidId
                    )
                }
            }
        }
    }

    fun getConditionResId(condition: Product.Condition): Int {
        return when (condition) {
            Product.Condition.New -> R.string.product_condition_new
            Product.Condition.Used -> R.string.product_condition_used
        }
    }
}

data class ProductDetailsUiState(
    val productId: String? = null,
    val productDetails: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null
)

fun Product.getConditionResId(): Int {
    return when (Product.Condition.from(condition)) {
        Product.Condition.New -> R.string.product_condition_new
        Product.Condition.Used -> R.string.product_condition_used
    }
}