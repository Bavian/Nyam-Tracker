package com.bavian.nyam.tracker.presentation.navigation

sealed interface AppRoute {
    val path: String

    data object Main : AppRoute {
        override val path: String = "main"
    }

    data object SetProduct : AppRoute {
        override val path: String = "set_product"
        const val ARG_PRODUCT_ID = "productId"
        val routeWithArgs: String = "$path?$ARG_PRODUCT_ID={$ARG_PRODUCT_ID}"
    }

    data object ProductsList : AppRoute {
        override val path: String = "products_list"
    }
}
