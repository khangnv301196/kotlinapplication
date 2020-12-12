package com.testing.kotlinapplication.ui.staff.order

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.ui.staff.order.modelcontract.NavigateID
import kotlinx.android.synthetic.main.fragment_order_staff.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class OrderStaffFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = FragmentAdapter(childFragmentManager, lifecycle)
        pager.adapter = adapter
        var names: Array<String> = arrayOf("Xử\nlý", "Giao\nhàng", "Đã\ngiao", "Đã thanh toán", "Đơn\nhủy")
        TabLayoutMediator(tab_layout, pager, false) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onNavigate(data: NavigateID) {
        if (data.isNavigate) {

        }
    }

}
