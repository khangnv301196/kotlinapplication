package com.testing.kotlinapplication.ui.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.testing.kotlinapplication.R

class OrderFragment : Fragment() {

    private lateinit var btn_proceed: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_order, container, false)
        mapping(itemView)
        return itemView
    }

    fun mapping(view: View) {
        btn_proceed = view.findViewById(R.id.btn_proceed)
        btn_proceed.setOnClickListener {
            findNavController().navigate(R.id.orderCompleteFragment)
        }
    }


}