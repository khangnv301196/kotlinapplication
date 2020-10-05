package com.testing.kotlinapplication.ui.staff.customerlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.ui.staff.StaffActivity

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as StaffActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as StaffActivity).showBottomNavigation()
    }

}
