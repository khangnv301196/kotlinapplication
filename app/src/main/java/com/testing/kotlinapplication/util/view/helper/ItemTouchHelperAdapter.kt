package com.testing.kotlinapplication.util.view.helper

interface ItemTouchHelperAdapter {
    fun onMoveItem(from: Int, to: Int)
    fun onRemoveItem(from: Int)
}