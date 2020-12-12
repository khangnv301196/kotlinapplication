package com.testing.kotlinapplication.ui.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.Data
import com.testing.kotlinapplication.network.model.ProductRespone
import com.testing.kotlinapplication.ui.recent.ProductAdapter
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), ProductAdapter.Itemclick {

    private lateinit var mContext: Context
    private lateinit var productAdapter: ProductAdapter
    private lateinit var mList: ArrayList<Data>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mList = ArrayList()
        productAdapter = ProductAdapter(mList, mContext, this)
        rv_search.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = productAdapter
        }
        search_item.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text != null) {
                    doGetSearch(text, object : DataCallBack<ProductRespone> {
                        override fun Complete(respon: ProductRespone) {
                            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
                            mList.clear()
                            mList.addAll(respon.data)
                            productAdapter.notifyDataSetChanged()
                        }

                        override fun Error(error: Throwable) {

                        }
                    })
                }
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                return false
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    fun doGetSearch(keyword: String, callBack: DataCallBack<ProductRespone>) {
        var param = HashMap<String, String>()
        param.put("search", keyword)
        param.put("page", "1")
        var compositeDisposable = CompositeDisposable(
            ServiceBuilder.buildService()
                .getProduct(Preference(mContext).getValueString(Constant.TOKEN), param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callBack.Complete(it)
                }, {
                    callBack.Error(it)
                })
        )
    }

    override fun onItemClick(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailProductFragment(1, id)
        findNavController().navigate(action)
    }

}
