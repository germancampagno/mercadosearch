package com.germancampagno.mercadosearch.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Categories : Screen("categories")

    data object Search : Screen("search")

    data object SearchCategory : Screen("search/{categoryId}/{categoryName}") {
        const val categoryIdArg = "categoryId"
        const val categoryNameArg = "categoryName"
        val arguments = listOf(
            navArgument("categoryId") { type = NavType.StringType },
            navArgument("categoryName") { type = NavType.StringType }
        )

        fun createRouteWithArgs(categoryId: String, categoryName: String): String =
            "search/$categoryId/$categoryName"
    }

    data object ProductDetails : Screen("product/{productId}") {
        const val productIdArg = "productId"
        const val routeWithArgs: String = "product/{$productIdArg}"
        val arguments = listOf(
            navArgument(productIdArg) { type = NavType.StringType }
        )

        fun createRoute(productId: String): String = "product/$productId"
    }
}