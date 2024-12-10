package com.germancampagno.mercadosearch.data.model

sealed class ErrorMessage {
    data object InvalidId : ErrorMessage()
    data class Exception(val message: String) : ErrorMessage()
}