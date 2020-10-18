package com.testing.kotlinapplication.ui.detailproduct

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.roomdata.ShopDatabase
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference

class AddCartUseCase(private var context: Context) {

    fun doCheckCart(): LiveData<CardModel> {
        return ShopRepository.getCardByUserID(
            context,
            Preference(context).getValueInt(Constant.USER_ID)
        )
    }

    fun doCreateNewCart() {
        ShopRepository.doAddNewCart(context, Preference(context).getValueInt(Constant.USER_ID))
    }
}