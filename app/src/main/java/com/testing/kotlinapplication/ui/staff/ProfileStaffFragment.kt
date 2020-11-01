package com.testing.kotlinapplication.ui.staff

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.ll_info
import kotlinx.android.synthetic.main.fragment_profile_staff.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileStaffFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile_staff, container, false)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLayout()
        ll_info_staff.setOnClickListener {
            doNavigatietoDetail()
        }
    }

    fun setUpLayout() {
        if (Preference(mContext).getValueBoolien(Constant.IS_LOGIN, false) == true) {
            context?.let {
                ShopRepository.getUser(it, Preference(mContext).getValueInt(Constant.USER_ID))!!
                    .observe(viewLifecycleOwner,
                        Observer {
                            txt_acc_name_staff.text = it.TenDangNhap
                            txt_acc_mail_staff.text = it.email
                            txt_acc_phone_staff.text = it.SDT
                            txt_acc_address_staff.text = it.DiaChi_SoNha
                            if (!it.HinhAnh.isEmpty()) {
                                Glide.with(mContext).load(it.HinhAnh)
                                    .into(avatar)
                            }
                        })
            }
        }
    }

    fun doNavigatietoDetail() {
        var bundle = Bundle()
        bundle.putBoolean("STAFF", true)
        findNavController().navigate(R.id.staff_todetail, bundle)
    }

}
