package com.testing.kotlinapplication.ui.staff.order.childrenfragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.testing.kotlinapplication.ui.staff.order.OrderStaffFragment
import com.testing.kotlinapplication.ui.staff.order.OrderStaffFragmentDirections
import com.testing.kotlinapplication.ui.staff.order.modelcontract.ProceedModel
import com.testing.kotlinapplication.ui.staff.order.modelcontract.UpdatingModel
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_updating.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class UpdatingFragment : Fragment(), ItemClick {

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
        // Inflate the layout for this fragment'
        var view = inflater.inflate(R.layout.fragment_updating, container, false)
        mContext = view.context
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        mAdapter = context?.let { ItemDeliveryAdapter(it, mList, this) }!!
        rv_update.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        doLoadUpdating(object : DataCallBack<OrderResponse> {
            override fun Complete(respon: OrderResponse) {
                progress_update.visibility = View.GONE
                mList.addAll(respon)
                mAdapter.notifyDataSetChanged()
                rv_update.apply {
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
                if(mList.size==0){
                    txt_empty_update.visibility=View.VISIBLE
                }else{
                    txt_empty_update.visibility=View.GONE
                }
            }

            override fun Error(error: Throwable) {
                Toast.makeText(mContext, "Error Load", Toast.LENGTH_SHORT).show()
                

            }
        })

    }

    fun doLoadUpdating(dataCallBack: DataCallBack<OrderResponse>) {
        var param = HashMap<String, String>()
        param.put("TrangThai", "2")
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

    fun doDelivery(data: OrderResponseItem, dataCallBack: DataCallBack<RegisterRespone>) {
        var param = HashMap<String, String>()
        param.put("id", "${data.id}")
        param.put("TrangThai", "3")
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onCompleteData(data: ProceedModel) {
        Log.d("Update", "Onstage")
        if (data.isProceed) {
            mList.add(0, data.data)
            mAdapter.notifyDataSetChanged()
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onDelivery(data: UpdatingModel) {
        if (!data.isUpdating) {
            doDelivery(data.data, object : DataCallBack<RegisterRespone> {
                override fun Complete(respon: RegisterRespone) {
                    Log.d("Updating", "Complete")
                    mList.remove(data.data)
                    mAdapter.notifyDataSetChanged()
                    data.isUpdating = true
                    data.data.TrangThai = 3
                    EventBus.getDefault().post(data)
                }

                override fun Error(error: Throwable) {
                    Log.d("Updating", "ERROR")
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
