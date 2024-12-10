package com.germancampagno.mercadosearch.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.ErrorMessage

@Composable
fun SimpleError(errorMessage: ErrorMessage?) {
    val message: String = when (errorMessage) {
        is ErrorMessage.InvalidId -> stringResource(R.string.product_detail_error_invalid_id)
        is ErrorMessage.Exception -> errorMessage.message
        else -> stringResource(R.string.generic_error_unknown)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error: $message")
    }
}
