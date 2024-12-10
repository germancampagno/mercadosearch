package com.germancampagno.mercadosearch.ui.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.ui.theme.MercadoSearchTheme
import com.germancampagno.mercadosearch.ui.util.BackButton
import com.germancampagno.mercadosearch.ui.util.GenericLoadingIndicator
import com.germancampagno.mercadosearch.ui.util.SimpleError

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit,
    onNavigationClick: () -> Unit,
    categoryName: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchPlaceHolder = categoryName ?: stringResource(R.string.mercadolibre)

    Scaffold(
        topBar = {
            SearchTopAppBar(
                onBackClick = onNavigationClick,
                onSearchQueryChanged = viewModel::onQueryChange,
                onSearchTriggered = viewModel::searchProducts,
                hint = stringResource(
                    R.string.top_app_bar_search_placeholder, searchPlaceHolder
                ),
                searchQuery = uiState.query,
                shouldRequestFocus = uiState.categoryId == null
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
                    LazyColumn(modifier = Modifier.padding(padding)) {
                        itemsIndexed(uiState.searchResults) { index, product ->
                            val conditionDisplayName = stringResource(
                                viewModel.getConditionResId(
                                    Product.Condition.from(product.condition)
                                )
                            )

                            ProductItem(
                                product = product,
                                conditionDisplayName = conditionDisplayName,
                                onProductClick = onProductClick,
                                shouldShowDivider = index > 0
                            )

                            if (index == uiState.searchResults.lastIndex && !uiState.isLoadingMore) {
                                viewModel.loadMoreProducts()
                            }
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                GenericLoadingIndicator()
                            }
                        }
                    }
                }
            }

        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopAppBar(
    searchQuery: String,
    hint: String,
    shouldRequestFocus: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = { BackButton(onBackClick) },
        title = {
            SearchTextField(
                query = searchQuery,
                onQueryChange = onSearchQueryChanged,
                onSearch = onSearchTriggered,
                hint = hint,
                shouldRequestFocus = shouldRequestFocus
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

@Composable
fun SearchTextField(
    query: String,
    hint: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    shouldRequestFocus: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearch(query)
    }

    TextField(
        value = query,
        onValueChange = { if (query != it) onQueryChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(16.dp)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearch(query)
                    true
                } else {
                    false
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = hint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        },
        shape = RoundedCornerShape(32.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.generic_search),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onQueryChange("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.generic_clear_search),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
    )

    if (shouldRequestFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}


@Preview
@Composable
private fun SearchToolbarPreview() {
    MercadoSearchTheme {
        SearchTopAppBar(
            searchQuery = "",
            hint = "Hint",
            onBackClick = {},
            onSearchQueryChanged = {},
            onSearchTriggered = {},
            shouldRequestFocus = true
        )
    }
}