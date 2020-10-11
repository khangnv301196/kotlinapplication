package com.testing.kotlinapplication.ui.recent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.testing.kotlinapplication.R
import kotlin.collections.ArrayList

class ProductAdapter(
    private val mList: ArrayList<String>,
    private val mContext: Context,
    private val itemClick: Itemclick
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindView(data: String) {
            var img = mView.findViewById(R.id.img_product) as ImageView
            Glide.with(mView)
                .load("https://static.toiimg.com/thumb/msid-77328879,width-640,resizemode-4/77328879.jpg")
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(50)
                    )
                )
                .into(img)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_grid, parent, false) as View
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {

        if (mList == null) {
            return 0
        } else {
            return mList.size;
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindView(mList.get(position))
        holder.itemView.setOnClickListener({ view -> itemClick.onItemClick() })
    }

    interface Itemclick {
        fun onItemClick()
    }

}