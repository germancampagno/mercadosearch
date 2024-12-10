package com.germancampagno.mercadosearch.data.model

data class SearchResponse(val results: List<Product>, val paging: Paging) {
    data class Paging(val total: Int)
}