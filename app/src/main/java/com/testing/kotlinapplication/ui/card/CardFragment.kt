package com.testing.kotlinapplication.ui.card

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.util
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import com.testing.kotlinapplication.util.view.helper.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.fragment_card.*


class CardFragment : Fragment(), DataClick {

    private lateinit var mAdapter: CartAdapter
    private lateinit var mList: ArrayList<ProductsModel>
    private lateinit var usecase: CartUseCase
    private lateinit var progresDialog: Dialog
    private var type = 0

    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        var view = inflater.inflate(R.layout.fragment_card, container, false)
        preference = Preference(view.context)
        usecase = CartUseCase(view.context, viewLifecycleOwner)
        progresDialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        mAdapter = CartAdapter(view.context, mList, this)
        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(mAdapter))
        rv_cart.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        itemTouchHelper.attachToRecyclerView(rv_cart)
        ll_checkout.setOnClickListener({ v -> doNavigateToOrder(v) })
        usecase.doGetAllProductFromCart(object : DataCallBack<CardModel> {
            override fun Complete(respon: CardModel) {
                respon.Id?.let {
                    usecase.doGetAllProductByCartId(it,
                        object : DataCallBack<List<ProductsModel>> {
                            override fun Complete(respon: List<ProductsModel>) {
                                mList.clear()
                                mList.addAll(respon)
                                mAdapter.notifyDataSetChanged()
                                doResetTotal()
                            }

                            override fun Error(error: Throwable) {

                            }
                        })
                }
            }

            override fun Error(error: Throwable) {
            }
        })

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

    override fun onItemClick(data: ProductsModel) {
        context?.let { ShopRepository.doUpdateProduct(it, data) }
        doResetTotal();
    }

    override fun onRemove(from: Int) {
        var product = mList.get(from)
        context?.let { ShopRepository.doDeletProduct(it, product) }
        mList.removeAt(from)
        mAdapter.notifyItemRemoved(from)
        doResetTotal();
    }

    fun doResetTotal() {
        var total = 0
        for (i in mList) {
            total = total + i.price * i.Total
        }
        txt_total.setText("Total : ${util.doFormatPrice(total)} đ")
    }
}