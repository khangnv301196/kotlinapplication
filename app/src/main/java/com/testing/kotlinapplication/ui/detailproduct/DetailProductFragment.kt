package com.testing.kotlinapplication.ui.detailproduct

import android.app.Dialog
import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.DetailProductReponse
import com.testing.kotlinapplication.network.model.DetailProductType
import com.testing.kotlinapplication.network.model.RegisterRespone
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import info.hoang8f.android.segmented.SegmentedGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.android.synthetic.main.fragment_product_ware_house.*
import kotlinx.android.synthetic.main.item_grid.view.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : Fragment() {

    private lateinit var btn_add: LinearLayout
    private val args: DetailProductFragmentArgs by navArgs()
    private lateinit var preference: Preference
    private lateinit var progressDialog: Dialog
    private lateinit var useCase: AddCartUseCase
    private lateinit var segment: SegmentedGroup
    private lateinit var txt_price_dialog: TextView
    private lateinit var dialog: BottomSheetDialog
    private lateinit var mContext: Context
    private var quantity = 1
    private var detailid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        var view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        mContext = view.context
        preference = Preference(view.context)
        progressDialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        progressDialog.show()
        var bottomsheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(view.context)
        dialog.setContentView(bottomsheet)
        useCase = AddCartUseCase(view.context, viewLifecycleOwner)
        mapping(view)
        setListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener({ v -> activity?.onBackPressed() })
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var titleIsShowing = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = "Detail Product"
                    titleIsShowing = true
                } else if (titleIsShowing) {
                    collapsing_toolbar.title =
                        " "//careful there should a space between double quote otherwise it wont work
                    titleIsShowing = false
                }
            }

        })
        doLoadApi(args.id)
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

    fun doLoadradio(listData: List<DetailProductType>) {
        segment.removeAllViews()
        for (i in listData) {
            var radioButton: RadioButton
            radioButton = layoutInflater.inflate(R.layout.radio_button_item, null) as RadioButton
            radioButton.id = i.id
            radioButton.text = i.Mau
            segment.addView(radioButton)
        }
        segment.updateBackground()
        segment.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                detailid = p1
            }
        })
    }

    fun setListener() {
        btn_add.setOnClickListener {
            dialog.show()
        }
    }

    fun doLoadApi(id: Int) {
        var param = HashMap<String, String>()
        param.put("id", "${id}")
        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getDetailProduct(preference.getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    setLayout(it)
                    progressDialog.hide()
                }, {
                    progressDialog.hide()
                })
        )
    }

    fun setLayout(data: DetailProductReponse) {
        configDialog(data)
        context?.let {
            Glide.with(it)
                .load(data.AnhChinh)
                .centerCrop()
                .into(img_product)
        }
        txt_title_product.setText(data.TenSP)
        val format = DecimalFormat("###,###,###,###")
        txt_price_z.setText("${format.format(data.DongGia.toInt())} đ")
        txt_detail.setText(Html.fromHtml(data.MoTa, 1))

    }


    fun configDialog(data: DetailProductReponse) {
        segment = dialog.findViewById(R.id.segment)!!
        txt_price_dialog = dialog.findViewById(R.id.txt_price)!!
        val format = DecimalFormat("###,###,###,###")
        txt_price_dialog.setText("${format.format(data.DongGia.toInt())} đ")
        doLoadradio(data.chi_tiet_san_pham)
        dialog.elegant.setNumber("${quantity}", true)
        dialog.elegant.setOnValueChangeListener(object : ElegantNumberButton.OnValueChangeListener {
            override fun onValueChange(view: ElegantNumberButton?, oldValue: Int, newValue: Int) {
                quantity = newValue
                var total = newValue * data.DongGia.toInt()
                val format = DecimalFormat("###,###,###,###")
                txt_price_dialog.setText("${format.format(total)} đ")
            }
        })
        dialog.btn_order?.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("Product", 1)
            dialog.hide()
            if (preference.getValueBoolien(Constant.IS_LOGIN, false)) {
                progressDialog.show()
                var cart = useCase.doCheckCart()
                cart.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        preference.save(Constant.HAS_CART, true)
                        it.Id?.let { it1 -> preference.save(Constant.CART_ID, it1) }
                    }
                })

                if (cart.hasActiveObservers()) {
                    Log.d("DETAIL", "${data.id}")
                    if (preference.getValueBoolien(Constant.HAS_CART, false)) {
                        var idCart = preference.getValueInt(Constant.CART_ID)
                        var product = ProductsModel(
                            data.TenSP,
                            idCart,
                            data.AnhChinh,
                            detailid,
                            quantity,
                            data.DongGia.toInt()
                        )
                        useCase.doAddProductByIDCart(product)
                    } else {
                        var product = ProductsModel(
                            data.TenSP,
                            0,
                            data.AnhChinh,
                            detailid,
                            quantity,
                            data.DongGia.toInt()
                        )
                        useCase.doCreateNewCart(product)
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    progressDialog.hide()
                    findNavController().navigate(R.id.cardFragment, bundle)
                }, 2000)
            } else {
                Toast.makeText(mContext, "Please Login to Order this Product", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}
