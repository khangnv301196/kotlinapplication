package com.testing.kotlinapplication.ui.detailprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.roomdata.ShopDatabase
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.fragment_detail_profile.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProfileFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_detail_profile, container, false)
        mContext = view.context
        preference = Preference(mContext)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            ShopRepository.getUser(it, Preference(it).getValueInt(Constant.USER_ID))
                ?.observe(viewLifecycleOwner,
                    Observer {
                        txt_name.setText(it.TenDangNhap)
                        email.setText(it.email)
                        gender.setText(it.GioiTinh)
                        phone.setText(it.SDT)
                        city.setText(it.DiaChi_ThanhPho)
                        district.setText(it.DiaChi_Quan)
                        address.setText(it.DiaChi_SoNha)
                        if (!it.HinhAnh.isEmpty()) {
                            Glide.with(view).load(it.HinhAnh).into(avatar)
                        }
                    })
        }
        btn_logout.setOnClickListener {
//            doLogout()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    fun doLogout() {
        var idCart = 0
        //delete user
        ShopRepository.doDeleteUserByID(mContext, preference.getValueInt(Constant.USER_ID))
        preference.clearSharedPreference()
        var intent = Intent(context, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        (activity as MainActivity).startActivity(intent)
        (activity as MainActivity).finish()
    }

}
