package com.testing.kotlinapplication.ui.staff.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.model.OrderResponseItem
import com.testing.kotlinapplication.ui.staff.order.modelcontract.CancelingModel
import com.testing.kotlinapplication.ui.staff.order.modelcontract.ProceedModel
import com.testing.kotlinapplication.ui.staff.order.modelcontract.UpdatingModel
import com.testing.kotlinapplication.util.util
import kotlinx.android.synthetic.main.item_delivery.view.*
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_history.view.address
import kotlinx.android.synthetic.main.item_history.view.btn_cancel
import kotlinx.android.synthetic.main.item_history.view.img_content
import kotlinx.android.synthetic.main.item_history.view.price
import kotlinx.android.synthetic.main.item_history.view.quantity
import kotlinx.android.synthetic.main.item_history.view.size
import kotlinx.android.synthetic.main.item_history.view.status
import kotlinx.android.synthetic.main.item_history.view.title_order
import kotlinx.android.synthetic.main.item_history.view.title_product
import org.greenrobot.eventbus.EventBus

class ItemDeliveryAdapter(
    private var mContext: Context,
    private var mList: ArrayList<OrderResponseItem>,
    private var itemClick: ItemClick
) :
    RecyclerView.Adapter<ItemDeliveryAdapter.ItemDeliberyViewHolder>() {
    class ItemDeliberyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: OrderResponseItem, itemClick: ItemClick) {
            var img = data.chi_tiet_hoa_don.get(0).AnhChinh
            var total = 0
            for (detail in data.chi_tiet_hoa_don) {
                total += detail.SoLuong * detail.Gia.toInt()
            }

            itemView.title_order_deli.setText("Create At: ${data.ngaytao}")
            Glide.with(itemView.context).load(img).into(itemView.img_content_deli)
            itemView.title_product_deli.setText(data.chi_tiet_hoa_don.get(0).TenSP)
            itemView.size_deli.setText(
                "Size: ${data.chi_tiet_hoa_don.get(0).KichThuoc}     Color: ${data.chi_tiet_hoa_don.get(
                    0
                ).Mau}"
            )

            itemView.quantity_deli.setText("x${data.chi_tiet_hoa_don.get(0).SoLuong}")
            itemView.price_deli.setText(
                "Total price: ${util.doFormatPrice(total)} đ"
            )
            itemView.address_deli.setText("Address: ${data.delivery_address.address},${data.delivery_address.district}.${data.delivery_address.city}")
            itemView.btn_proceed_deli.setOnClickListener {
                EventBus.getDefault().post(ProceedModel(false, data))
            }
            itemView.btn_cancel_deli.setOnClickListener {
                EventBus.getDefault().post(CancelingModel(false, data))
            }
            itemView.btn_delivery_deli.setOnClickListener {
                EventBus.getDefault().post(UpdatingModel(false, data))
            }

            when (data.TrangThai) {
                1 -> {
                    itemView.status_deli.setText("Procced")
                    itemView.btn_delivery_deli.visibility = View.GONE
                }
                2 -> {
                    itemView.status_deli.setText("Updating")
                    itemView.btn_proceed_deli.visibility = View.GONE
                    itemView.btn_cancel_deli.visibility = View.GONE
                }
                3 -> {
                    itemView.status_deli.setText("Complete")
                    itemView.btn_cancel_deli.visibility = View.INVISIBLE
                    itemView.btn_proceed_deli.visibility = View.INVISIBLE
                    itemView.btn_delivery_deli.visibility = View.INVISIBLE
                }
                4 -> {
                    itemView.status_deli.setText("Cancel")
                    itemView.btn_proceed_deli.visibility = View.INVISIBLE
                    itemView.btn_cancel_deli.visibility = View.INVISIBLE
                    itemView.btn_delivery_deli.visibility = View.INVISIBLE
                }
                5 -> {
                    itemView.status_deli.setText("Paid")
                    itemView.btn_proceed_deli.visibility = View.INVISIBLE
                    itemView.btn_cancel_deli.visibility = View.INVISIBLE
                    itemView.btn_delivery_deli.visibility = View.INVISIBLE
                }
            }
            itemView.setOnClickListener {
                itemClick.OnItemCLick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDeliberyViewHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.item_delivery, parent, false)
        return ItemDeliberyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemDeliberyViewHolder, position: Int) {
        holder.bindView(mList.get(position), itemClick)
    }
}