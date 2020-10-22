package com.testing.kotlinapplication.ui.card

import com.testing.kotlinapplication.repository.ProductsModel

interface DataClick {
    fun onItemClick(data: ProductsModel)

    fun onRemove(from: Int)
}