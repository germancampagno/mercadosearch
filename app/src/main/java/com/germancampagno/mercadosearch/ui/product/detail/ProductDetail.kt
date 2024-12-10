package com.germancampagno.mercadosearch.ui.product.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.util.DebugLogger
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.Product

@Composable
fun ProductDetail(
    product: Product,
    conditionDisplayName: String,
    padding: PaddingValues,
    onCTAClick: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { product.pictures?.size ?: 0 }
    )

    Column(
        Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = conditionDisplayName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.title,
            style = MaterialTheme.typography.labelLarge,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val imageLoader = ImageLoader.Builder(context = LocalContext.current)
                .logger(DebugLogger())
                .build()
            AsyncImage(
                model = product.pictures?.get(page)?.url,
                contentDescription = null,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.formattedPrice.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp),
            letterSpacing = TextUnit(0.36F, TextUnitType.Sp),
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(Modifier.padding(horizontal = 16.dp)) {
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
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCTAClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.surfaceDim,
                MaterialTheme.colorScheme.surfaceBright
            )
        ) {
            Text(
                stringResource(
                    R.string.product_detail_cta_buy
                ),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        val description = if (!product.description.isNullOrEmpty()) {
            product.description
        } else {
            stringResource(R.string.product_detail_no_description)
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}
