package com.germancampagno.mercadosearch.ui.product.detail


import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.ui.util.BackButton
import com.germancampagno.mercadosearch.ui.util.GenericLoadingIndicator
import com.germancampagno.mercadosearch.ui.util.SimpleError

@Composable
fun ProductDetailsScreen(
    onNavigationClick: () -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ProductDetailTopAppBar(
                onBackClick = onNavigationClick,
            )
        },
        content = { padding ->
            when {
                uiState.isLoading -> {
                    GenericLoadingIndicator()
                }

                uiState.errorMessage != null -> {
                    SimpleError(errorMessage = uiState.errorMessage)
                }

                else -> {
                    val product = uiState.productDetails
                    product?.let {
                        val conditionDisplayName = stringResource(
                            viewModel.getConditionResId(
                                Product.Condition.from(product.condition)
                            )
                        )
                        ProductDetail(product, conditionDisplayName, padding, {
                            product.permalink?.let {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                context.startActivity(intent)
                            }
                        })
                    }

                }
            }

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailTopAppBar(onBackClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.product_detail_top_app_bar_title)) },
        navigationIcon = { BackButton(onBackClick = onBackClick) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )

}