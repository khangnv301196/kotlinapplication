package com.testing.kotlinapplication.ui.staff.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import com.testing.kotlinapplication.R
import kotlinx.android.synthetic.main.fragment_order_staff.*


class OrderStaffFragment : Fragment() {


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
        var names: Array<String> = arrayOf("Proceed", "Update", "Delivery","Paid", "Cancel")
        TabLayoutMediator(tab_layout, pager,false) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

}
