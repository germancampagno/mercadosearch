package com.germancampagno.mercadosearch.ui.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.germancampagno.mercadosearch.data.model.Category

@Composable
internal fun CategoryItem(
    category: Category, onCategoryClick: (String, String) -> Unit, shouldShowDivider: Boolean
) {
    // Adding only a 'end' padding to the parent column so the click ripple effect doesn't reach the whole screen width
    Column(
        Modifier
            .padding(end = 8.dp)
            .fillMaxWidth()
            .clickable {
                onCategoryClick(
                    category.id, category.name
                )
            }) {
        if (shouldShowDivider) {
            HorizontalDivider(Modifier.padding(start = 16.dp))
        }

        Text(
            text = category.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}