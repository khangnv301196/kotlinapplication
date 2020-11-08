package com.testing.kotlinapplication.ui.detailorder

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.ChiTietHoaDon
import com.testing.kotlinapplication.network.model.Data
import com.testing.kotlinapplication.network.model.OrderResponse
import com.testing.kotlinapplication.ui.staff.StaffActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.util
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_order.*

/**
 * A simple [Fragment] subclass.
 */
class DetailOrderFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var progressDialog: Dialog
    private val param: DetailOrderFragmentArgs by navArgs()
    private lateinit var mList: ArrayList<ChiTietHoaDon>
    private lateinit var mAdapter: DetailOrderAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (param.type == 1) {
            (activity as MainActivity).hideBottomNavigation()
        } else {
            (activity as StaffActivity).hideBottomNavigation()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (param.type == 1) {
            (activity as MainActivity).showBottomNavigation()
        } else {
            (activity as StaffActivity).showBottomNavigation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_detail_order, container, false)
        mContext = view.context
        progressDialog = ProgressDialogutil.setProgressDialog(mContext, "Loading")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.show()
        mList = ArrayList()
        mAdapter = DetailOrderAdapter(mContext, mList)
        rv_order_detail.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        doLoadDetailOrder(param.id, object : DataCallBack<OrderResponse> {
            override fun Complete(respon: OrderResponse) {
                Log.d("DATA", respon.toString())
                var data = respon.get(0)
                txt_name_od.setText(data.delivery_address.name)
                txt_number_od.setText(data.delivery_address.phone)
                txt_city_od.setText(data.delivery_address.city)
                txt_district_od.setText(data.delivery_address.district)
                txt_address_od.setText(data.delivery_address.address)
                txt_status_ode.setText(doFilter(data.TrangThai))
                txt_total_od.setText(getTotal(data.chi_tiet_hoa_don))
                mList.addAll(data.chi_tiet_hoa_don)
                mAdapter.notifyDataSetChanged()
                progressDialog.hide()
            }

            override fun Error(error: Throwable) {
                Log.d("DATA", error.toString())
            }

        })
    }

    fun doLoadDetailOrder(id: Int, callBack: DataCallBack<OrderResponse>) {
        var param = HashMap<String, String>()
//        param.put("MaKhachHang", "${Preference(mContext).getValueInt(Constant.USER_ID)}")
        param.put("id", "${id}")
        Log.d("DEBUG", param.toString())
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getListOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    callBack.Complete(it)
                }, {
                    callBack.Error(it)
                })
        )
    }

    fun doFilter(status: Int): String {
        when (status) {
            1 -> return "Processing"
            2 -> return "Updating"
            3 -> return "Deliveried"
            4 -> return "Cancel"
            5 -> return "Complete Pay"
            else -> return "Loading"
        }
    }

    fun getTotal(mList: List<ChiTietHoaDon>): String {
        var total = 0
        for (i in mList) {
            total += i.Gia.toInt() * i.SoLuong
        }
        return "${util.doFormatPrice(total)} đ"
    }

}
