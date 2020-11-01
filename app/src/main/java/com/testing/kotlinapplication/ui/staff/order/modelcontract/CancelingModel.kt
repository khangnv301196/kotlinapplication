package com.testing.kotlinapplication.ui.staff.order.modelcontract

import com.testing.kotlinapplication.network.model.OrderResponseItem

data class CancelingModel(var isCancel: Boolean, var data: OrderResponseItem)