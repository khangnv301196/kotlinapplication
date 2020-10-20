package com.testing.kotlinapplication.ui.detailproduct

import android.app.Dialog
import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.AsyncListUtil
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.DetailProductReponse
import com.testing.kotlinapplication.network.model.RegisterRespone
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.view.MyInCreaseView
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_detail_product.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : Fragment() {

    private lateinit var btn_add: LinearLayout
    private val args: DetailProductFragmentArgs by navArgs()
    private lateinit var preference: Preference
    private lateinit var progressDialog: Dialog
    private lateinit var useCase: AddCartUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        var view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        preference = Preference(view.context)
        progressDialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        progressDialog.show()
        useCase = AddCartUseCase(view.context)
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


    fun setListener() {
        btn_add.setOnClickListener {
            val bottomsheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            dialog?.setContentView(bottomsheet)
            dialog?.btn_order?.setOnClickListener {
                var bundle = Bundle()
                bundle.putInt("Product", 1)
//                progressDialog.show()
//                doUpdateCart(args.id, 3, object : DataCallBack<RegisterRespone> {
//                    override fun Complete(respon: RegisterRespone) {
//                        findNavController().navigate(R.id.cardFragment, bundle)
//                        dialog?.hide()
//                        progressDialog.hide()
//                    }
//
//                    override fun Error(error: Throwable) {
//                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
//                        dialog?.hide()
//                        progressDialog.hide()
//
//                    }
//                })

//                context?.let { it1 -> ShopRepository.doAddNewCart(it1, preference.getValueInt(Constant.USER_ID)) }
//                context?.let { it1 ->
//                    ShopRepository.getCardByUserID(
//                        it1,
//                        preference.getValueInt(Constant.USER_ID)
//                    ).observe(viewLifecycleOwner, Observer {
//                        Log.d("KHANGNVDEBUG", it.Id.toString())
//                    })
//                }
//                var product: ProductsModel
//                product = ProductsModel("new omega", 1, "IMAGE", 1234, 3, 10000)
//                context?.let { it1 -> ShopRepository.doAddProductToCard(it1, product) }

            }
            dialog?.show()
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
//                    Toast.makeText(context, "has Data", Toast.LENGTH_SHORT).show()
                    progressDialog.hide()
                }, {
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    progressDialog.hide()
                })
        )
    }

    fun setLayout(data: DetailProductReponse) {
        context?.let {
            Glide.with(it)
                .load(data.AnhChinh)
                .centerCrop()
                .into(img_product)
        }
        txt_title_product.setText(data.TenSP)
        val format = DecimalFormat("###,###,###,###")
        txt_price.setText("${format.format(data.DongGia.toInt())}đ")
        txt_detail.setText(Html.fromHtml(data.MoTa, 1))
    }

    fun doUpdateCart(id: Int, quantity: Int, callback: DataCallBack<RegisterRespone>) {
        var bodys = HashMap<String, String>()
        bodys.put("MaChiTietSanPham", "${id}")
        bodys.put("MaKhachHang", "${preference.getValueInt(Constant.USER_ID)}")
        bodys.put("SoLuong", "1")
        Log.d("KHANGNVDEBUG", "${preference.getValueInt(Constant.USER_ID)}")
        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postNewCart(preference.getValueString(Constant.TOKEN), bodys)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.Complete(it)
                }, {
                    callback.Error(it)
                })
        )
    }

}

