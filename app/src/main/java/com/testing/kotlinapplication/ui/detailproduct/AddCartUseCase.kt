package com.testing.kotlinapplication.ui.detailproduct

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.roomdata.ShopDatabase
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference

class AddCartUseCase(private var context: Context, private var lifecycleOwner: LifecycleOwner) {

    fun doCheckCart(): LiveData<CardModel> {
        return ShopRepository.getCardByUserID(
            context,
            Preference(context).getValueInt(Constant.USER_ID)
        )
    }

    fun doAddProductByIDCart(product: ProductsModel) {
//        var count = 0
//        ShopRepository.doCheckProduct(context, product).observe(lifecycleOwner, Observer {
//            if (it != null) {
//                var total = it.Total + product.Total
//                it.Total = total
//                ShopRepository.doUpdateProduct(context,it)
//                count = count + 1
//                Log.d("DEBUG", count.toString())
//
//            } else {
//                Log.d("DEBUG2", it.toString())
//                ShopRepository.doAddProductToCard(context, product)
//            }
//        })

        ShopRepository.doAddProductToCard(context, product)

    }

    fun doCreateNewCart(data: ProductsModel) {
        ShopRepository.doAddNewCart(context, Preference(context).getValueInt(Constant.USER_ID))
        Handler(Looper.myLooper()).postDelayed(Runnable {
            ShopRepository.getCardByUserID(
                context,
                Preference(context).getValueInt(Constant.USER_ID)
            ).observe(lifecycleOwner, Observer {
                data.idCart = it.Id!!
                ShopRepository.doAddProductToCard(context, data)
            })
        }, 2000)
    }
}