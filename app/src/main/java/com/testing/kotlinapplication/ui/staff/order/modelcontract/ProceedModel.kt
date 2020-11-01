package com.testing.kotlinapplication.ui.staff.order.modelcontract

import com.testing.kotlinapplication.network.model.OrderResponseItem

data class ProceedModel(var isProceed: Boolean, var data: OrderResponseItem)