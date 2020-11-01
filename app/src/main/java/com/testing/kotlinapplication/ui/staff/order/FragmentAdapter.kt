package com.testing.kotlinapplication.ui.staff.order


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.testing.kotlinapplication.ui.staff.order.childrenfragment.*


class FragmentAdapter(fm: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fm, lifeCycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> return ProceedFragment()
            1 -> return UpdatingFragment()
            2 -> return CompleteFragment()
            3 -> return PaidFragment()
            4 -> return CancelFragment()
            else -> return ProceedFragment()
        }
    }

}