package com.testing.kotlinapplication.ui.orderhistory

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.OrderResponse
import com.testing.kotlinapplication.network.model.OrderResponseItem
import com.testing.kotlinapplication.network.model.RegisterRespone
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_order_history.*


class OrderHistoryFragment : Fragment(), AdapterView.OnItemSelectedListener, OrderCallback {

    private lateinit var mAdapter: OrderHistoryAdapter
    private lateinit var mContext: Context
    private lateinit var progressDialog: Dialog

    private lateinit var listData: ArrayList<OrderResponseItem>
    private lateinit var listProceed: ArrayList<OrderResponseItem>
    private lateinit var listUpdate: ArrayList<OrderResponseItem>
    private lateinit var listComplete: ArrayList<OrderResponseItem>
    private lateinit var listPaid: ArrayList<OrderResponseItem>
    private lateinit var listCancel: ArrayList<OrderResponseItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_order_history, container, false)
        mContext = view.context
        progressDialog = ProgressDialogutil.setProgressDialog(mContext, "Loading...")
        progressDialog.show()
        initData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = context?.let { OrderHistoryAdapter(it, listData, this) }!!
        ArrayAdapter.createFromResource(
            mContext,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        rv_history.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        doLoadListOrder(object : DataCallBack<OrderResponse> {
            override fun Complete(respon: OrderResponse) {
                Log.d("DEBUG", respon.toString())
                listData.addAll(respon)
                for (item in respon) {
                    when (item.TrangThai) {
                        1 -> listProceed.add(item)
                        2 -> listUpdate.add(item)
                        3 -> listComplete.add(item)
                        4 -> listCancel.add(item)
                        5 -> listPaid.add(item)
                    }
                }
                mAdapter.notifyDataSetChanged()

                progressDialog.hide()
            }

            override fun Error(error: Throwable) {
                Log.e("DEBUG", error.toString())
                progressDialog.hide()
                Toast.makeText(mContext, "Error from server", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun initData() {
        listData = ArrayList()
        listProceed = ArrayList()
        listUpdate = ArrayList()
        listComplete = ArrayList()
        listCancel = ArrayList()
        listPaid = ArrayList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when (pos) {
            0 -> {
                mAdapter.mList = listData
                mAdapter.notifyDataSetChanged()
            }
            1 -> {
                mAdapter.mList = listProceed
                mAdapter.notifyDataSetChanged()
            }
            2 -> {
                mAdapter.mList = listUpdate
                mAdapter.notifyDataSetChanged()
            }
            3 -> {
                mAdapter.mList = listComplete
                mAdapter.notifyDataSetChanged()
            }
            4 -> {
                mAdapter.mList = listPaid
                mAdapter.notifyDataSetChanged()
            }
            5 -> {
                mAdapter.mList = listCancel
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    fun doLoadListOrder(callback: DataCallBack<OrderResponse>) {
        var param = HashMap<String, String>()
        param.put("MaKhachHang", "${Preference(mContext).getValueInt(Constant.USER_ID)}")
        Log.d("DEBUG", param.toString())
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getListOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    callback.Complete(it)
                }, {
                    callback.Error(it)
                })
        )
    }

    fun doUpdateStatusOrder(
        param: HashMap<String, String>,
        dataCallBack: DataCallBack<RegisterRespone>
    ) {
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getUpdateOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    dataCallBack.Complete(it)
                }, {
                    dataCallBack.Error(it)
                })
        )
    }

    override fun onCancel(data: OrderResponseItem) {
        var param = HashMap<String, String>()
        param.put("id", "${data.id}")
        param.put("TrangThai", "4")
        doUpdateStatusOrder(param, object : DataCallBack<RegisterRespone> {
            override fun Complete(respon: RegisterRespone) {
                Log.d("History", "Cancel complete")
                listData.remove(data)
                listProceed.remove(data)
                data.TrangThai = 4
                listCancel.add(0, data)
                mAdapter.notifyDataSetChanged()
            }

            override fun Error(error: Throwable) {
                Log.d("History", "Cancel error")
            }
        })
    }

    override fun onPaid(data: OrderResponseItem) {
        var param = HashMap<String, String>()
        param.put("id", "${data.id}")
        param.put("TrangThai", "5")
        doUpdateStatusOrder(param, object : DataCallBack<RegisterRespone> {
            override fun Complete(respon: RegisterRespone) {
                Log.d("History", "paid complete")
                listData.remove(data)
                listComplete.remove(data)
                data.TrangThai = 5
                listPaid.add(0, data)
                mAdapter.notifyDataSetChanged()
            }

            override fun Error(error: Throwable) {

            }
        })
    }

    override fun onItemClick(id: Int) {
        val action =
            OrderHistoryFragmentDirections.actionOrderHistoryFragmentToDetailOrderFragment(1, id)
        findNavController().navigate(action)
    }


}