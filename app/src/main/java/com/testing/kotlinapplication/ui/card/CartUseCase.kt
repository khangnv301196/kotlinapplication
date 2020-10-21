package com.testing.kotlinapplication.ui.card

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import java.security.acl.Owner

class CartUseCase(private var context: Context, private var lifecycleOwner: LifecycleOwner) {
    fun doGetAllProductFromCart(callback: DataCallBack<CardModel>) {
        ShopRepository.getCardByUserID(context, Preference(context).getValueInt(Constant.USER_ID))
            .observe(lifecycleOwner,
                Observer {
                    if (it != null) {
                        callback.Complete(it)
                    } else {
                        var t = Throwable()
                        callback.Error(t)
                    }
                })
    }

    fun doGetAllProductByCartId(id: Int, callback: DataCallBack<List<ProductsModel>>) {
        ShopRepository.doGetAllProductByCardid(context, id).observe(lifecycleOwner, Observer {
            if (!it.isEmpty()) {
                callback.Complete(it)
            } else {
                var t = Throwable()
                callback.Error(t)
            }
        })
    }
}