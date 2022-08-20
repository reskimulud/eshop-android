package com.mankart.eshop.core.utils

import com.mankart.eshop.core.BuildConfig

object Constants {
    const val EXTRA_PRODUCT_ID = "productId"
    const val EXTRA_TRANSACTION_ID = "transactionId"

    private const val DOMAIN_URI = "eshop://"
    const val PRODUCT_URI = "${DOMAIN_URI}product_fragment"
    const val DETAIL_PRODUCT_URI = "${DOMAIN_URI}detail_product_fragment"
    const val CART_URI = "${DOMAIN_URI}cart_fragment"

    const val DATA_STORE_NAME = "application"
    const val EMPTY_DATA_STORE = "not_set_yet"

    const val BASE_URL = "https://${BuildConfig.HOST_NAME}/"
}