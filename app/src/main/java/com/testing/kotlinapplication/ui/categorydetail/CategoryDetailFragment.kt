package com.testing.kotlinapplication.ui.categorydetail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.Data
import com.testing.kotlinapplication.network.model.ProductRespone
import com.testing.kotlinapplication.ui.recent.ProductAdapter
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_category_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryDetailFragment : Fragment(), ProductAdapter.Itemclick {
    private lateinit var mList: ArrayList<Data>
    private lateinit var mAdapter: ProductAdapter
    private lateinit var dialog: Dialog
    private val args: CategoryDetailFragmentArgs by navArgs()
    lateinit var gridlayoutManager: GridLayoutManager

    private var visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var isLoading: Boolean = false
    private var page = 1;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_category_detail, container, false)
        dialog = ProgressDialogutil.setProgressDialog(view.context, "Loading")
        initData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).resetActionBar()
        gridlayoutManager = GridLayoutManager(view.context, 2)
        gridlayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            //Each item occupies 1 span by default.
            override fun getSpanSize(p0: Int): Int {
                return when (mAdapter.getItemViewType(p0)) {
                    //returns total no of spans, to occupy full sapce for progressbar
                    ProductAdapter.VIEW_TYPE_PROGRESS -> gridlayoutManager.spanCount
                    ProductAdapter.VIEW_TYPE_DATA -> 1
                    else -> -1
                }
            }
        }
        rv_category_detail.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = gridlayoutManager
        }

        addScrollerListener()

        dialog.show()
        doLoadApi(1, object : DataCallBack<ProductRespone> {
            override fun Complete(respon: ProductRespone) {
                mList.addAll(respon.data)
                mAdapter.notifyDataSetChanged()
                dialog.hide()
            }

            override fun Error(error: Throwable) {
            }
        })
    }

    private fun initData() {
        mList = ArrayList()
        mAdapter = context?.let { ProductAdapter(mList, it, this) }!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onItemClick(id: Int) {
        val action =
            CategoryDetailFragmentDirections.actionCategoryDetailFragmentToDetailProductFragment(
                0,
                id
            )
        findNavController().navigate(action)
    }

    private fun addScrollerListener() {
        rv_category_detail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //total no. of items
                totalItemCount = gridlayoutManager.itemCount
                //last visible item position
                lastVisibleItem = gridlayoutManager.findLastCompletelyVisibleItemPosition()
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loadMore()
                    isLoading = true
                }
            }
        })
    }

    private fun loadMore() {
        var emptyData = Data()
        emptyData.viewType = 1
        mList.add(mList.size, emptyData)
        mAdapter.notifyDataSetChanged()
        page = page + 1
        doLoadApi(page, object : DataCallBack<ProductRespone> {
            override fun Complete(respon: ProductRespone) {
                rv_category_detail.postDelayed(Runnable {
                    //removes load item in list.
                    mList.removeAt(mList.size - 1)
                    var curent_size = mAdapter.itemCount
                    mAdapter.notifyItemRemoved(mList.size - 1)
                    mList.addAll(respon.data)
//                        productAdapter.notifyItemRangeInserted(curent_size,it.data.size)
                    mAdapter.notifyDataSetChanged()
                    isLoading = false
                }, 2000)
            }

            override fun Error(error: Throwable) {
            }

        })
    }


    fun doLoadApi(page: Int, dataCallBack: DataCallBack<ProductRespone>) {
        var params = HashMap<String, String>()
        params.put("MaLoaiSanPham", "${args.id}")
        params.put("page", "${page}")
        var compositeDisposable: CompositeDisposable
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService().getProduct("", params)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    dataCallBack.Complete(it)
                }, {
                    dataCallBack.Error(it)
                })
        )
    }
}
