package com.testing.kotlinapplication.ui.recent

import android.content.Context
import android.icu.text.DecimalFormat
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
import com.testing.kotlinapplication.network.model.Data
import kotlinx.android.synthetic.main.item_grid.view.*
import kotlin.collections.ArrayList

class ProductAdapter(
    private val mList: ArrayList<Data>,
    private val mContext: Context,
    private val itemClick: Itemclick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_DATA = 0;
        const val VIEW_TYPE_PROGRESS = 1;
    }

    class ProductViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindView(data: Data) {
            val format = DecimalFormat("###,###,###,###")
            mView.txt_name_product.setText(data.TenSP)
            mView.txt_price.setText("${format.format(data.DongGia.toInt())} đ")
            var img = mView.findViewById(R.id.img_product) as ImageView
            Glide.with(mView)
                .load(data.AnhChinh)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(50)
                    )
                )
                .into(img)

        }
    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA -> {
                val view =
                    LayoutInflater.from(mContext).inflate(R.layout.item_grid, parent, false)
                ProductViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(mContext).inflate(R.layout.progressbar, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }

    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size;
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = mList.get(position).viewType
        return when (viewtype) {
            1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bindView(mList.get(position))
            holder.itemView.setOnClickListener({ view -> itemClick.onItemClick() })
        }
    }

    interface Itemclick {
        fun onItemClick()
    }

}