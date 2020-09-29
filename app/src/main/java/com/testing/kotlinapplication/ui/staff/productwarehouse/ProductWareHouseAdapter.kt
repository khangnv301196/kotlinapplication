package com.testing.kotlinapplication.ui.staff.productwarehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.testing.kotlinapplication.R

class ProductWareHouseAdapter(private var mContext: Context, private var mList: ArrayList<String>) :
    RecyclerView.Adapter<ProductWareHouseAdapter.ProductWareHouseViewHolder>() {
    class ProductWareHouseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(item: String, context: Context) {
            val radius = context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            var img = itemView.findViewById(R.id.img_product) as ImageView
            Glide.with(itemView)
                .load("https://media.karousell.com/media/photos/products/2019/07/05/dx_crocodile_crack_full_bottle_kamen_rider_rogue_1562334404_80a67b20.jpg")
                .transform(RoundedCorners(radius))
                .transition(DrawableTransitionOptions.withCrossFade())
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
        holder.bindView(mList.get(position), mContext)
    }
}