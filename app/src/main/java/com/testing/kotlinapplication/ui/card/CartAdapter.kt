package com.testing.kotlinapplication.ui.card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.model.DanhSachGioHang
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.util.util
import com.testing.kotlinapplication.util.view.helper.ItemTouchHelperAdapter
import kotlinx.android.synthetic.main.row_card.view.*

class CartAdapter(
    val mContext: Context,
    var mList: ArrayList<ProductsModel>,
    var dataClick: DataClick
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() ,ItemTouchHelperAdapter{
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ProductsModel, dataClick: DataClick) {
            itemView.txt_product_name.setText(item.productName)
            itemView.elegant.setNumber("${item.Total}", true)
            itemView.value.setText("${util.doFormatPrice(item.Total * item.price)}đ")
            itemView.elegant.setOnValueChangeListener(object :
                ElegantNumberButton.OnValueChangeListener {
                override fun onValueChange(
                    view: ElegantNumberButton?,
                    oldValue: Int,
                    newValue: Int
                ) {
                    itemView.value.setText("${util.doFormatPrice(newValue * item.price)}đ")
                    item.Total = newValue
                    dataClick.onItemClick(item)
                }
            })
            Glide.with(itemView).load(item.image).into(itemView.img_product)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.row_card, parent, false)
        return CartViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bindView(mList.get(position), dataClick);
    }

    override fun onMoveItem(from: Int, to: Int) {

    }

    override fun onRemoveItem(from: Int) {
        dataClick.onRemove(from)
    }

}