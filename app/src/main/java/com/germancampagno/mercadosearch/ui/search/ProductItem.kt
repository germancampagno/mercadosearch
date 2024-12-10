package com.germancampagno.mercadosearch.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.util.DebugLogger
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.Product

@Composable
fun ProductItem(
    product: Product,
    conditionDisplayName: String,
    onProductClick: (String) -> Unit,
    shouldShowDivider: Boolean
) {
    if (shouldShowDivider) {
        HorizontalDivider()
    }
    ListItem(
        leadingContent = {
            val imageLoader =
                ImageLoader.Builder(context = LocalContext.current)
                    .logger(DebugLogger())
                    .build()
            AsyncImage(
                product.thumbnail,
                contentDescription = null,
                imageLoader = imageLoader,
                modifier = Modifier.size(64.dp)
            )
        },
        headlineContent = {
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            )
        },
        supportingContent = {
            Column {
                Row {
                    Text(
                        text = product.formattedPrice.toString(),
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = conditionDisplayName,
                        fontStyle = MaterialTheme.typography.labelSmall.fontStyle
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        Modifier
                            .height(MaterialTheme.typography.labelMedium.fontSize.value.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = product.formattedAddress.toString(),
                        fontStyle = MaterialTheme.typography.labelSmall.fontStyle
                    )
                }
            }
        },
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = { onProductClick(product.id) }
            ),
    )
}