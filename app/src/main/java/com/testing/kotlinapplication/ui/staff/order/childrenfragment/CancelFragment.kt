package com.testing.kotlinapplication.ui.staff.order.childrenfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.OrderResponse
import com.testing.kotlinapplication.network.model.OrderResponseItem
import com.testing.kotlinapplication.ui.staff.order.ItemDeliveryAdapter
import com.testing.kotlinapplication.ui.staff.order.modelcontract.CancelingModel
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cancel.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
class CancelFragment : Fragment() {
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
        var view = inflater.inflate(R.layout.fragment_cancel, container, false)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        mAdapter = context?.let { ItemDeliveryAdapter(it, mList) }!!
        rv_cancel.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        doLoadCancel(object : DataCallBack<OrderResponse> {
            override fun Complete(respon: OrderResponse) {
                progress_cancel.visibility = View.GONE
                mList.addAll(respon)
                mAdapter.notifyDataSetChanged()
                rv_cancel.apply {
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
            }

            override fun Error(error: Throwable) {
                Toast.makeText(mContext, "Error Load", Toast.LENGTH_SHORT).show()
                Log.d("DEBUG", error.toString())
            }

        })
    }

    fun doLoadCancel(dataCallBack: DataCallBack<OrderResponse>) {
        var param = HashMap<String, String>()
        param.put("TrangThai", "4")
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onCancelItem(data: CancelingModel) {
        if (data.isCancel) {
            mList.add(0, data.data)
            mAdapter.notifyDataSetChanged()
        }
    }
}
