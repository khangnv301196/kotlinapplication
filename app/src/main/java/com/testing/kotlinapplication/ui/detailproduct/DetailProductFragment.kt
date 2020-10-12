package com.testing.kotlinapplication.ui.detailproduct

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_detail_product.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : Fragment() {

    private lateinit var btn_add: LinearLayout
    private val args: DetailProductFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        var view=inflater.inflate(R.layout.fragment_detail_product, container, false)
        mapping(view)
        setListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener({ v -> activity?.onBackPressed() })
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var titleIsShowing = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = "New Omega"
                    titleIsShowing = true
                } else if (titleIsShowing) {
                    collapsing_toolbar.title =
                        " "//careful there should a space between double quote otherwise it wont work
                    titleIsShowing = false
                }
            }

        })
        (activity as MainActivity).setActionBar(toolbar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideAppBar()
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showAppBar()
        (activity as MainActivity).resetActionBar()
        if (args.turn == 1) {
            (activity as MainActivity).showBottomNavigation()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.cart_menu -> {
                var bundle = Bundle()
                bundle.putInt("Product", 1)
                findNavController().navigate(R.id.cardFragment, bundle)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu, menu)
    }

    fun mapping(view: View) {
        btn_add = view.findViewById(R.id.bottom)

    }

    fun setListener() {
        btn_add.setOnClickListener {
            val bottomsheet=layoutInflater.inflate(R.layout.bottom_sheet,null)
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            dialog?.setContentView(bottomsheet)
            dialog?.show()
        }

    }

}

