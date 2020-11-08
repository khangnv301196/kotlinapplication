package com.testing.kotlinapplication.ui.staff.order.childrenfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.OrderResponse
import com.testing.kotlinapplication.network.model.OrderResponseItem
import com.testing.kotlinapplication.network.model.RegisterRespone
import com.testing.kotlinapplication.ui.staff.order.ItemClick
import com.testing.kotlinapplication.ui.staff.order.ItemDeliveryAdapter
import com.testing.kotlinapplication.ui.staff.order.OrderStaffFragmentDirections
import com.testing.kotlinapplication.ui.staff.order.modelcontract.CancelingModel
import com.testing.kotlinapplication.ui.staff.order.modelcontract.ProceedModel
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_proceed.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
class ProceedFragment : Fragment(), ItemClick {

    private lateinit var mList: ArrayList<OrderResponseItem>
    private lateinit var mAdapter: ItemDeliveryAdapter
    private lateinit var mContext: Context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_proceed, container, false)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        mAdapter = context?.let { ItemDeliveryAdapter(it, mList, this) }!!
        rv_proceed.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        doLoadProoced(object : DataCallBack<OrderResponse> {
            override fun Complete(respon: OrderResponse) {
                progress_proceed.visibility = View.GONE
                mList.addAll(respon)
                mAdapter.notifyDataSetChanged()
                rv_proceed.apply {
                    alpha = 0f
                    scaleX = 0f
                    scaleY = 0f
                    visibility = View.VISIBLE
                    animate()
                        .alpha(1f)
                        .scaleX(1f).scaleY(1f)
                        .setDuration(1000)
                        .setListener(null)
                }
                if (mList.size == 0) {
                    txt_empty_proceed.visibility = View.VISIBLE
                } else {
                    txt_empty_proceed.visibility = View.GONE
                }
            }

            override fun Error(error: Throwable) {
                Toast.makeText(mContext, "Error Load", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun doLoadProoced(dataCallBack: DataCallBack<OrderResponse>) {
        var param = HashMap<String, String>()
        param.put("TrangThai", "1")
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getListOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    dataCallBack.Complete(it)
                }, {
                    dataCallBack.Error(it)
                })
        )
    }

    fun doProceedData(data: OrderResponseItem, callback: DataCallBack<RegisterRespone>) {
        var param = HashMap<String, String>()
        param.put("id", "${data.id}")
        param.put("TrangThai", "2")
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getUpdateOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    callback.Complete(it)
                }, {
                    callback.Error(it)
                })
        )
    }

    fun doDeleteData(data: OrderResponseItem, callback: DataCallBack<RegisterRespone>) {
        var param = HashMap<String, String>()
        param.put("id", "${data.id}")
        param.put("TrangThai", "4")
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getUpdateOrder(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    callback.Complete(it)
                }, {
                    callback.Error(it)
                })
        )
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onCancelItem(data: CancelingModel) {
        Log.d("Proceed", "cancel")
        if (!data.isCancel) {
            doDeleteData(data.data, object : DataCallBack<RegisterRespone> {
                override fun Complete(respon: RegisterRespone) {
                    Log.d("Proceed", "Delete Complete")
                    mList.remove(data.data)
                    mAdapter.notifyDataSetChanged()
                    data.isCancel = true
                    data.data.TrangThai = 4
                    EventBus.getDefault().post(data)
                }

                override fun Error(error: Throwable) {
                    Log.d("Proceed", "Delete Error")
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onUpdateModel(data: ProceedModel) {
        Log.d("Proceed", "onstage")
        if (!data.isProceed) {
            doProceedData(data.data, object : DataCallBack<RegisterRespone> {
                override fun Complete(respon: RegisterRespone) {
                    Log.d("Proceed", "Update Complete")
                    mList.remove(data.data)
                    mAdapter.notifyDataSetChanged()
                    data.isProceed = true
                    data.data.TrangThai = 2
                    EventBus.getDefault().post(data)
                }

                override fun Error(error: Throwable) {
                    Log.d("Proceed", "Update Error")
                }
            })
        }
    }

    override fun OnItemCLick(id: Int) {
        val action =
            OrderStaffFragmentDirections.actionOrderStaffFragmentToDetailOrderFragment2(id, 2)
        findNavController().navigate(action)
    }
}
