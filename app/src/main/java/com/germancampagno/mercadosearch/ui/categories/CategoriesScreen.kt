package com.germancampagno.mercadosearch.ui.categories

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.ui.util.GenericLoadingIndicator
import com.germancampagno.mercadosearch.ui.util.SimpleError

@Composable
fun CategoriesScreen(
    onSearchClick: () -> Unit,
    onCategoryClick: (String, String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CategoriesTopAppBar(
                title = stringResource(R.string.app_name),
                actionIcon = Icons.Rounded.ShoppingCart,
                actionIconContentDescription = stringResource(
                    id = R.string.top_app_bar_action_icon_description,
                ),
                onSearchClick = onSearchClick,
                onActionClick = {
                    val videoUrl = "https://youtu.be/dQw4w9WgXcQ?si=YhdJaeQzoaa461Ze"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                    context.startActivity(intent)
                },
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

                uiState.categories.isNotEmpty() -> {
                    LazyColumn(modifier = Modifier.padding(padding)) {
                        itemsIndexed(uiState.categories) { index, category ->
                            CategoryItem(category, onCategoryClick, index > 0)
                        }

                    }
                }
            }

        })
}


@Composable
private fun SearchButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            MaterialTheme.colorScheme.surfaceDim,
            MaterialTheme.colorScheme.surfaceBright
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onTertiary,
        )

        Text(
            stringResource(
                R.string.top_app_bar_search_placeholder, stringResource(R.string.mercadolibre)
            ),
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesTopAppBar(
    title: String,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    onActionClick: (() -> Unit)? = null,
    onSearchClick: () -> Unit
) {
    Column(Modifier.background(MaterialTheme.colorScheme.secondary)) {
        TopAppBar(
            title = { Text(text = title) },
            actions = {
                if (actionIcon != null && onActionClick != null) {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = actionIconContentDescription
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            modifier = modifier
        )

        SearchButton(onClick = onSearchClick)
    }
}
