package com.testing.kotlinapplication.ui.staff.order


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentAdapter(fm: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fm, lifeCycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return HolderFragment.newInstance("new", "new")
    }

}