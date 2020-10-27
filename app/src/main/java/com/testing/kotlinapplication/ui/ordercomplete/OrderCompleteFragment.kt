package com.testing.kotlinapplication.ui.ordercomplete

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.ui.ordercomplete.model.OrderItem
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.fragment_order_complete.*

/**
 * A simple [Fragment] subclass.
 */
class OrderCompleteFragment : Fragment() {
    private lateinit var btn_next: LinearLayout
    private lateinit var img: ImageView
    private lateinit var mContext: Context
    private lateinit var listData: LiveData<List<ProductsModel>>
    private lateinit var listPost: ArrayList<OrderItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_order_complete, container, false)
        mContext = itemView.context
        mapping(itemView)
        setListener()
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doOrder()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            img.apply {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .scaleX(1f).scaleY(1f)
                    .setDuration(1000)
                    .setListener(null)
            }
            btn_next.apply {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .scaleX(1f).scaleY(1f)
                    .setDuration(1000)
                    .setListener(null)
            }
            progressBar.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })
        }, 2000)
    }

    fun mapping(view: View) {
        img = view.findViewById(R.id.img_check)
        btn_next = view.findViewById(R.id.btn_next)
    }

    fun setListener() {
        btn_next.setOnClickListener {
//            var intent = Intent(context, MainActivity::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            (activity as MainActivity).startActivity(intent)
//            (activity as MainActivity).finish()
//            context?.let { it1 -> showDialog(it1) }

        }
    }

    fun showDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.rating_dialog)
        dialog.show()
    }

    fun doOrder() {
        Toast.makeText(
            mContext,
            "${arguments?.getString("name")} " +
                    "\n${arguments?.getString("phone")} " +
                    "\n${arguments?.getString("city")}" +
                    "\n${arguments?.getString("district")}" +
                    "\n${arguments?.getString("address")}" +
                    "\n${Preference(mContext).getValueInt(Constant.CART_ID)}",
            Toast.LENGTH_SHORT
        ).show()
        listData = ShopRepository.doGetAllProductByCardid(
            mContext,
            Preference(mContext).getValueInt(Constant.CART_ID)
        )
        listPost = ArrayList()
        listData.observe(viewLifecycleOwner, Observer {
            for (item in it) {
                listPost.add(OrderItem(item.idServer, item.Total, item.price))
            }
            var gson = Gson()
            var jsonString = gson.toJson(listPost)
            Log.d("Debug", jsonString)
        })
    }
}