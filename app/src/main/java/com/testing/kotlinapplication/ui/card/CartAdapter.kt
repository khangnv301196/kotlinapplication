package com.testing.kotlinapplication.ui.card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.model.DanhSachGioHang
import com.testing.kotlinapplication.repository.ProductsModel
import kotlinx.android.synthetic.main.row_card.view.*

class CartAdapter(val mContext: Context, var mList: ArrayList<ProductsModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ProductsModel) {
            itemView.txt_product_name.setText(item.productName)
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
        holder.bindView(mList.get(position));
    }

}