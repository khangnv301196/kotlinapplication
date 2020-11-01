package com.testing.kotlinapplication.ui.staff.order.modelcontract

import com.testing.kotlinapplication.network.model.OrderResponseItem

data class UpdatingModel(var isUpdating: Boolean, var data: OrderResponseItem)