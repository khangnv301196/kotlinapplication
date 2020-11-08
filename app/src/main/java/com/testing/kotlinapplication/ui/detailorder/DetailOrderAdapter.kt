package com.testing.kotlinapplication.ui.detailorder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.model.ChiTietHoaDon
import com.testing.kotlinapplication.util.util
import kotlinx.android.synthetic.main.item_order_detail.view.*
import kotlinx.android.synthetic.main.item_product.view.*


class DetailOrderAdapter(
    val mContext: Context,
    var mList: ArrayList<ChiTietHoaDon>
) : RecyclerView.Adapter<DetailOrderAdapter.DetailOrderViewHolder>() {
    class DetailOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ChiTietHoaDon) {
            itemView.txt_product_od.setText(item.TenSP)
            itemView.quantity_od.setText("Quantity: ${item.SoLuong}")
            var total = item.Gia.toInt() * item.SoLuong
            itemView.value_od.setText("${util.doFormatPrice(total)} đ")
            Glide.with(itemView).load(item.AnhChinh).into(itemView.img_product_od)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOrderViewHolder {
        val itemView =
            LayoutInflater.from(mContext).inflate(R.layout.item_order_detail, parent, false)
        return DetailOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DetailOrderViewHolder, position: Int) {
        holder.bindView(mList.get(position))
    }
}