package com.testing.kotlinapplication.ui.recent

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.Data
import com.testing.kotlinapplication.network.model.Slider
import com.testing.kotlinapplication.network.model.TopResponse
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
        setHasOptionsMenu(true)
        dialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        dialog.show()
        page = 1
        preference = Preference(view.context)
        mapping(view)
        doLoadApi(object : DataCallBack<TopResponse> {
            override fun Complete(respon: TopResponse) {
                setupUI(view.context, flipper, respon.sliders)
                mList.clear()
                mList.addAll(respon.danhSachSanPham)
                productAdapter.notifyDataSetChanged()
                dialog.hide()
            }

            override fun Error(error: Throwable) {
                dialog.hide()
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flipper.setInAnimation(view.context, android.R.anim.slide_in_left)
        flipper.setOutAnimation(view.context, android.R.anim.slide_out_right)
        mListCoupon.add("https://www.kaijubattle.net/uploads/2/9/5/7/29570123/711976090_orig.jpg")
        mListCoupon.add("https://i0.wp.com/post.healthline.com/wp-content/uploads/2020/07/Best_Baby_Clothes_1296x728.png?w=1155&h=1528")
        mListCoupon.add("https://i.pinimg.com/originals/02/23/3e/02233e46ec1eca2967460ec0c2648290.jpg")
        couponAdapter.notifyDataSetChanged()

        swiperefresh.setOnRefreshListener {
            container.animate()
                .alpha(0f)
                .setDuration(100)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })
            doLoadApi(object : DataCallBack<TopResponse> {
                override fun Complete(respon: TopResponse) {
                    swiperefresh.isRefreshing = false
                    setupUI(view.context, flipper, respon.sliders)
                    mList.clear()
                    mList.addAll(respon.danhSachSanPham)
                    page = 0
                    productAdapter.notifyDataSetChanged()
                    container.apply {
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null)
                    }
                }

                override fun Error(error: Throwable) {
                    swiperefresh.isRefreshing = false
                    Toast.makeText(context, "Load Fail", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                findNavController().navigate(R.id.action_recentFragment_to_searchFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun mapping(view: View) {
        //create list
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

    fun setupUI(context: Context, viewFlipper: ViewFlipper, mList: List<Slider>) {

        viewFlipper.removeAllViews()
        for (slider in mList) {
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
            Glide.with(context).load(slider.Anh).centerCrop().into(imageView)
            viewFlipper.addView(imageView)
        }

        flipper.startFlipping()

    }

    fun doLoadApi(dataCallBack: DataCallBack<TopResponse>) {
        var compositeDisposable = CompositeDisposable(
            ServiceBuilder.buildService()
                .getDashBoard()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    dataCallBack.Complete(it)
                }, {
                    dataCallBack.Error(it)
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