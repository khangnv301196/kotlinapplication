package com.testing.kotlinapplication.ui.staff.productwarehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.testing.kotlinapplication.R
import kotlinx.android.synthetic.main.fragment_product_ware_house.*

class ProductWareHouseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var mAdapter: ProductWareHouseAdapter
    private lateinit var mList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_ware_house, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList<String>()
        for (i in 0..10) {
            mList.add(" ")
        }
        mAdapter = context?.let { ProductWareHouseAdapter(it, mList) }!!
        rv_product.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

}