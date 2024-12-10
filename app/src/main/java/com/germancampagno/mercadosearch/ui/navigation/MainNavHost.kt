package com.germancampagno.mercadosearch.ui.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.germancampagno.mercadosearch.ui.categories.CategoriesScreen
import com.germancampagno.mercadosearch.ui.product.detail.ProductDetailsScreen
import com.germancampagno.mercadosearch.ui.search.SearchScreen

@Composable
fun MainNavHost(navController: NavHostController = rememberNavController()) {
    Surface {
        NavHost(
            navController = navController,
            startDestination = Screen.Categories.route,
            modifier = Modifier
        ) {
            // TODO - Create routes in each screen
            // Categories Screen (Home)
            composable(route = Screen.Categories.route) {
                CategoriesScreen(onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }, onCategoryClick = { categoryId, categoryName ->
                    navController.navigate(
                        Screen.SearchCategory.createRouteWithArgs(
                            categoryId, categoryName
                        )
                    )
                })
            }

            // Search Screen
            composable(
                route = Screen.SearchCategory.route, arguments = Screen.SearchCategory.arguments
            ) { backStackEntry ->
                val categoryName =
                    backStackEntry.arguments?.getString(Screen.SearchCategory.categoryNameArg)

                launchSearchScreen(navController, categoryName)
            }

            composable(route = Screen.Search.route) {
                launchSearchScreen(navController)
            }

            // Product Detail Screen
            composable(
                route = Screen.ProductDetails.routeWithArgs,
                arguments = Screen.ProductDetails.arguments
            ) {
                ProductDetailsScreen(onNavigationClick = {
                    navController.popBackStack()
                })
            }
        }
    }
}

@Composable
private fun launchSearchScreen(navController: NavHostController, categoryName: String? = null) {
    SearchScreen(onProductClick = { productId ->
        navController.navigate(Screen.ProductDetails.createRoute(productId))
    }, onNavigationClick = {
        navController.popBackStack()
    }, categoryName = categoryName
    )
}