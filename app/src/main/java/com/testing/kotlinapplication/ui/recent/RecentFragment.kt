package com.testing.kotlinapplication.ui.recent

import android.content.Context
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_recent.*

/**
 * A simple [Fragment] subclass.
 */
class RecentFragment : Fragment(), ProductAdapter.Itemclick {

    private lateinit var rv_top: RecyclerView
    private lateinit var rv_promotion: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var couponAdapter: CouponAdapter
    private lateinit var mList: ArrayList<String>
    private lateinit var mListPromotion: ArrayList<String>
    private lateinit var mListCoupon: ArrayList<String>
    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_recent, container, false) as View
        preference = Preference(view.context)
        doLoadApi()
        mapping(view)
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
        rv_top = view.findViewById(R.id.rv_top)
        rv_promotion = view.findViewById(R.id.promotion)
        mList = ArrayList()
        for (i in 0..9) {
            mList.add("new omega");
        }
        productAdapter = ProductAdapter(mList, view.context, this)
        rv_top.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = productAdapter
        }
        mListCoupon = ArrayList()
        couponAdapter = CouponAdapter(view.context, mListCoupon)
        rv_promotion.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = couponAdapter
        }

    }

    fun setupUI(context: Context, viewFlipper: ViewFlipper) {
        mListPromotion = ArrayList()
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
                    Toast.makeText(context, "get data", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                })
        )
    }

    override fun onItemClick() {
        val action = RecentFragmentDirections.actionRecentFragmentToDetailProductFragment(1, 3)
        findNavController().navigate(action)
    }

}
