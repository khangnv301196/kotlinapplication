package com.testing.kotlinapplication.ui.orderhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.model.OrderResponseItem
import com.testing.kotlinapplication.util.util
import kotlinx.android.synthetic.main.item_history.view.*
import okhttp3.internal.Util

class OrderHistoryAdapter(
    private var mContext: Context,
    var mList: ArrayList<OrderResponseItem>,
    private var callback: OrderCallback
) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: OrderResponseItem, callback: OrderCallback) {
            try {
                var img = data.chi_tiet_hoa_don.get(0).AnhChinh

                itemView.title_order.setText("Tạo vào: ${data.ngaytao}")
                Glide.with(itemView.context).load(img).into(itemView.img_content)
                itemView.title_product.setText(data.chi_tiet_hoa_don.get(0).TenSP)
                itemView.size.setText(
                    "Loại: ${data.chi_tiet_hoa_don.get(0).KichThuoc}     Màu sắc: ${data.chi_tiet_hoa_don.get(
                        0
                    ).Mau}"
                )

                itemView.quantity.setText("x${data.chi_tiet_hoa_don.get(0).SoLuong}")
                itemView.price.setText(
                    "${util.doFormatPrice(
                        data.chi_tiet_hoa_don.get(0).SoLuong * data.chi_tiet_hoa_don.get(
                            0
                        ).Gia.toInt()
                    )} đ"
                )
            }catch (e:Exception){}
            itemView.address.setText("Địa chỉ: ${data.delivery_address.address},${data.delivery_address.district}.${data.delivery_address.city}")

            itemView.btn_cancel.visibility = View.VISIBLE
            itemView.btn_complete.visibility = View.VISIBLE

            itemView.btn_complete.setOnClickListener {
                callback.onPaid(data)
            }
            itemView.btn_cancel.setOnClickListener {
                callback.onCancel(data)
            }
            when (data.TrangThai) {
                1 -> {
                    itemView.status.setText("Xử lý")
                    itemView.btn_complete.visibility = View.INVISIBLE
                }
                2 -> {
                    itemView.status.setText("Cập nhật")
                    itemView.btn_complete.visibility = View.INVISIBLE
                    itemView.btn_cancel.visibility = View.INVISIBLE
                }
                3 -> {
                    itemView.status.setText("Hoàn thành")
                    itemView.btn_cancel.visibility = View.GONE
                }
                4 -> {
                    itemView.status.setText("Hủy")
                    itemView.btn_complete.visibility = View.INVISIBLE
                    itemView.btn_cancel.visibility = View.INVISIBLE
                }
                5 -> {
                    itemView.status.setText("Đã thanh toán")
                    itemView.btn_complete.visibility = View.INVISIBLE
                    itemView.btn_cancel.visibility = View.INVISIBLE
                }
            }
            itemView.setOnClickListener {
                callback.onItemClick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false)
        return OrderHistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (mList == null) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bindView(mList.get(position), callback)
    }

}