package com.mankart.eshop.core.utils

object Constants {
    const val EXTRA_PRODUCT_ID = "productId"
    const val EXTRA_TRANSACTION_ID = "transactionId"

    private const val DOMAIN_URI = "eshop://"
    const val PRODUCT_URI = "${DOMAIN_URI}product_fragment"
    const val DETAIL_PRODUCT_URI = "${DOMAIN_URI}detail_product_fragment"
    const val CART_URI = "${DOMAIN_URI}cart_fragment"

    const val EMPTY_DATA_STORE = "not_set_yet"
}