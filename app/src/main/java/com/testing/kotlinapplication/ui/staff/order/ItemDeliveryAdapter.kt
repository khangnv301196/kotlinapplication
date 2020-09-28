package com.testing.kotlinapplication.ui.staff.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testing.kotlinapplication.R

class ItemDeliveryAdapter(private var mContext: Context, private var mList: ArrayList<String>) :
    RecyclerView.Adapter<ItemDeliveryAdapter.ItemDeliberyViewHolder>() {
    class ItemDeliberyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDeliberyViewHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.item_delivery, parent, false)
        return ItemDeliberyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindViewHolder(holder: ItemDeliberyViewHolder, position: Int) {

    }
}