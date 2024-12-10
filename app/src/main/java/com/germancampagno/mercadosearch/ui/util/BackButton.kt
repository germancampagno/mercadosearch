package com.germancampagno.mercadosearch.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.germancampagno.mercadosearch.R

@Composable
fun BackButton(onBackClick: () -> Unit) {
    IconButton(onClick = { onBackClick() }) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.generic_back),
        )
    }
}