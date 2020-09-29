package com.testing.kotlinapplication.ui.staff.productwarehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.R

class ProductWareHouseAdapter(private var mContext: Context, private var mList: ArrayList<String>) :
    RecyclerView.Adapter<ProductWareHouseAdapter.ProductWareHouseViewHolder>() {
    class ProductWareHouseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(item: String) {
            var img=itemView.findViewById(R.id.img_product) as ImageView
            Glide.with(itemView)
                .load("https://cdn.mos.cms.futurecdn.net/ahevYTh9pWRzkNPF85MQhb-1200-80.jpg")
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductWareHouseViewHolder {
        var itemVIew = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false)
        return ProductWareHouseViewHolder(itemVIew)
    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindViewHolder(holder: ProductWareHouseViewHolder, position: Int) {
        holder.bindView(mList.get(position))
    }
}