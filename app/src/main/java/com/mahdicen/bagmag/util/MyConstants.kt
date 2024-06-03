package com.mahdicen.bagmag.util

import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.data.Product

const val KEY_PRODUCT_ARG = "productID"
const val KEY_Category_ARG = "category"

val EmptyProduct = Product(
    "1" ,
    "klfajskldf" ,
    "https://www.stviateurbagel.com/uploads/products/resized/20210915170652_75_30_svb_web_merch_photos_canvas_tote_bag_lifestyle_1.jpg",
    "",
    "890",
    "",
    "sadfafd",
    "",
    "",
    "4"
)

const val BaseUrl = "https://dunijet.ir/Projects/DuniBazaar/"

val CATEGORY = listOf(
    Pair("Backpack", R.drawable.ic_backpack),
    Pair("Handbag", R.drawable.ic_cat_handbag),
    Pair("Shopping", R.drawable.ic_cat_shopping),
    Pair("Tote", R.drawable.ic_cat_tote),
    Pair("Satchel", R.drawable.ic_cat_satchel),
    Pair("Clutch", R.drawable.ic_cat_clutch),
    Pair("Wallet", R.drawable.ic_cat_wallet),
    Pair("Sling", R.drawable.ic_cat_sling),
    Pair("Bucket", R.drawable.ic_cat_bucket),
    Pair("Quilted", R.drawable.ic_cat_quilted)
)

val TAGS = listOf(
    "Newest",
    "Best Sellers",
    "Most Visited",
    "Highest Quality"
)

const val PAYMENT_SUCCESS = 1
const val PAYMENT_PENDING = 0
const val PAYMENT_FAIL = -1
const val NO_PAYMENT = -2