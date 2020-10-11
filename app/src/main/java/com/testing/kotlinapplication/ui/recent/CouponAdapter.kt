package com.testing.kotlinapplication.ui.recent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.testing.kotlinapplication.R
import jp.wasabeef.blurry.Blurry

class CouponAdapter(private val context: Context, private val mList: ArrayList<String>) :
    RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {
    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(link: String) {
            var img = itemView.findViewById(R.id.img_background) as ImageView
            Glide.with(itemView)
                .load(link)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(50)
                    )
                )
                .into(img)
            Blurry.with(itemView.context)
                .radius(25)
                .sampling(2)
                .async()
                .animate(500).onto(itemView.findViewById(R.id.ll_viewg))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_promotion, parent, false)
        return CouponViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bindView(mList.get(position))
    }
}