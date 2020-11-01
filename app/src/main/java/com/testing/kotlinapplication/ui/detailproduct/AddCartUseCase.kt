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
        var update = false
        ShopRepository.doCheckProduct(context, product).observe(lifecycleOwner, Observer {
            if (it != null) {
                var total = it.Total + product.Total
                product.Total = total
                product.Id = it.Id
                update = true
            } else {
                Log.d("DEBUG2", it.toString())
                update = false

            }
        })

        Handler(Looper.myLooper()).postDelayed(Runnable {
            if (update == true) {
                ShopRepository.doUpdateProduct(context, product)
            } else {
                ShopRepository.doAddProductToCard(context, product)
            }
        }, 500)

    }

    fun doCreateNewCart(data: ProductsModel) {
        ShopRepository.doAddNewCart(context, Preference(context).getValueInt(Constant.USER_ID))
        Handler(Looper.myLooper()).postDelayed(Runnable {
            var cart = ShopRepository.getCardByUserID(
                context,
                Preference(context).getValueInt(Constant.USER_ID)
            )
            cart.observe(lifecycleOwner, Observer {
                Preference(context).save(Constant.CART_ID, it.Id!!)
            })
            if (cart.hasActiveObservers()) {
                data.idCart = Preference(context).getValueInt(Constant.CART_ID)
                ShopRepository.doAddProductToCard(context, data)
            }
        }, 300)
    }

}