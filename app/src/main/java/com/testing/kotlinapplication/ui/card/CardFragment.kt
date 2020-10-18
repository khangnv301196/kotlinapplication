package com.testing.kotlinapplication.ui.card

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.R
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.fragment_detail_product.*


class CardFragment : Fragment() {

    private lateinit var mAdapter: CartAdapter
    private lateinit var mList: ArrayList<String>
    private var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        for (i in 0..10) {
            mList.add("new")
        }
        mAdapter = CartAdapter(view.context, mList)
        rv_cart.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        ll_checkout.setOnClickListener({ v -> doNavigateToOrder(v) })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        type = arguments?.getInt("Product")!!
        if (type == 1) {
            (activity as MainActivity).resetActionBar()
            (activity as MainActivity).showAppBar()
        }
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        if (type == 1) {
            (activity as MainActivity).hideAppBar()
            (activity as MainActivity).hideBottomNavigation()
        } else {
            (activity as MainActivity).showBottomNavigation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.cart_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                Toast.makeText(context, "delete nha", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun doNavigateToOrder(v: View) {
        val action = CardFragmentDirections.actionCardFragmentToOrderFragment()
        findNavController().navigate(action)
    }


}