package com.testing.kotlinapplication.ui.staff.customerlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testing.kotlinapplication.R

/**
 * A simple [Fragment] subclass.
 */
class CustomerListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }

}
