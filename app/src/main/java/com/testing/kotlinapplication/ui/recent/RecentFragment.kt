package com.testing.kotlinapplication.ui.recent

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.Data
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product_ware_house.*
import kotlinx.android.synthetic.main.fragment_recent.*
import kotlinx.android.synthetic.main.fragment_recent.view.*

/**
 * A simple [Fragment] subclass.
 */
class RecentFragment : Fragment(), ProductAdapter.Itemclick {

    private lateinit var rv_top: RecyclerView
    private lateinit var rv_promotion: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var couponAdapter: CouponAdapter
    private lateinit var mList: ArrayList<Data>
    private lateinit var mListPromotion: ArrayList<String>
    private lateinit var mListCoupon: ArrayList<String>
    private lateinit var preference: Preference
    private lateinit var gridlayoutManager: GridLayoutManager
    private lateinit var dialog: Dialog

    private var pastVisiblesItems = 0
    private var totalItemCount = 0
    private var visibleItemCount = 0
    private var isLoading: Boolean = false
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_recent, container, false) as View
        dialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        dialog.show()
        page = 1
        preference = Preference(view.context)
        mapping(view)
        doLoadApi()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flipper.setInAnimation(view.context, android.R.anim.slide_in_left)
        flipper.setOutAnimation(view.context, android.R.anim.slide_out_right)
        setupUI(view.context, flipper)
        flipper.startFlipping()
        couponAdapter.notifyDataSetChanged()
    }

    fun mapping(view: View) {
        //create list
        mListPromotion = ArrayList()
        mList = ArrayList()
        mListCoupon = ArrayList()
        //mapping
        rv_top = view.findViewById(R.id.rv_top)
        rv_promotion = view.findViewById(R.id.promotion)
        productAdapter = ProductAdapter(mList, view.context, this)
        gridlayoutManager = GridLayoutManager(view.context, 2)
        gridlayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            //Each item occupies 1 span by default.
            override fun getSpanSize(p0: Int): Int {
                return when (productAdapter.getItemViewType(p0)) {
                    //returns total no of spans, to occupy full sapce for progressbar
                    ProductAdapter.VIEW_TYPE_PROGRESS -> gridlayoutManager.spanCount
                    ProductAdapter.VIEW_TYPE_DATA -> 1
                    else -> -1
                }
            }
        }

        rv_top.apply {
            setHasFixedSize(true)
            layoutManager = gridlayoutManager
            adapter = productAdapter
        }
        couponAdapter = CouponAdapter(view.context, mListCoupon)
        rv_promotion.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = couponAdapter
        }
        view.scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v != null) {
                    if (v.getChildAt(v.childCount - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                            scrollY > oldScrollY
                        ) {

                            visibleItemCount = gridlayoutManager.getChildCount();
                            totalItemCount = gridlayoutManager.getItemCount();
                            pastVisiblesItems = gridlayoutManager.findFirstVisibleItemPosition();
                            if (isLoading == false && ((visibleItemCount + pastVisiblesItems) >= totalItemCount)) {
                                loadMore()
                                isLoading = true
                            }

                        }
                    }
                }
            }
        })

    }

    fun setupUI(context: Context, viewFlipper: ViewFlipper) {

        mListPromotion.add("https://luatminhkhue.vn/LMK/article/mergepost/mau-thong-bao-thuc-hien-chuong-trinh-khuyen-mai-moi-nhat-nam-2018-97963.jpg")
        mListPromotion.add("https://hanoicomputercdn.com/media/news/18_0630_chuong_trinh_khuyen_mai_thang_lg_qua_me_ly.jpg")
        mListPromotion.add("https://www.bigc.vn/files/blog/cong-bo-ket-qua/luckydraw.jpg")
        mListPromotion.add("https://www.emart.com.vn/wp-content/uploads/vui-khai-truong-ngan-khuyen-mai-1715-960x480.jpg")
        mListPromotion.add("https://anh.24h.com.vn/upload/4-2016/images/2016-11-02/1478067064-khuyen-mai-mung-khai-truong.jpg")
        for (img in mListPromotion) {
            val imageView = ImageView(context)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.setMargins(0, 0, 0, 0)
            layoutParams.gravity = Gravity.CENTER
            imageView.layoutParams = layoutParams
            imageView.setOnClickListener {
                Toast.makeText(context, "new omega", Toast.LENGTH_SHORT).show()
            }
            Glide.with(context).load(img).centerCrop().into(imageView)
            viewFlipper.addView(imageView)
        }


        mListCoupon.add("https://www.kaijubattle.net/uploads/2/9/5/7/29570123/711976090_orig.jpg")
        mListCoupon.add("https://i0.wp.com/post.healthline.com/wp-content/uploads/2020/07/Best_Baby_Clothes_1296x728.png?w=1155&h=1528")
        mListCoupon.add("https://i.pinimg.com/originals/02/23/3e/02233e46ec1eca2967460ec0c2648290.jpg")

    }

    fun doLoadApi() {
        var param = HashMap<String, String>()
        param.put("page", "1")
        var compositeDisposable = CompositeDisposable(
            ServiceBuilder.buildService()
                .getProduct(preference.getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mList.addAll(it.data)
                    productAdapter.notifyDataSetChanged()
//                    Toast.makeText(context, "get data", Toast.LENGTH_SHORT).show()
                    dialog.hide()
                }, {
                    dialog.hide()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                })
        )
    }

    fun doLoadmore(page: Int) {
        var param = HashMap<String, String>()
        param.put("page", "${page}")
        var compositeDisposable = CompositeDisposable(
            ServiceBuilder.buildService()
                .getProduct(preference.getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    rv_top.postDelayed(Runnable {
                        //removes load item in list.
                        mList.removeAt(mList.size - 1)
                        var curent_size = productAdapter.itemCount
                        productAdapter.notifyItemRemoved(mList.size - 1)
                        mList.addAll(it.data)
//                        productAdapter.notifyItemRangeInserted(curent_size,it.data.size)
                        productAdapter.notifyDataSetChanged()
                        isLoading = false
                    }, 5000)

                }, {

                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                })
        )
    }

    private fun loadMore() {
        var emptyData = Data()
        emptyData.viewType = 1
        mList.add(mList.size, emptyData)
        productAdapter.notifyDataSetChanged()
        page = page + 1
        doLoadmore(page)

    }

    override fun onItemClick(id: Int) {
        val action = RecentFragmentDirections.actionRecentFragmentToDetailProductFragment(1, id)
        findNavController().navigate(action)
    }

}