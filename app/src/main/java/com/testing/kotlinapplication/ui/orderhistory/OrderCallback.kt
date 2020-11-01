package com.testing.kotlinapplication.ui.orderhistory

import com.testing.kotlinapplication.network.model.OrderResponseItem

interface OrderCallback {
    fun onCancel(data: OrderResponseItem)
    fun onPaid(data: OrderResponseItem)
}