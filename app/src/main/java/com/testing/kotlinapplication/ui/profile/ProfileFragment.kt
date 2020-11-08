package com.testing.kotlinapplication.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.fragment_detail_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers.Main

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false) as View
        preference = Preference(view.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLayout()
        ll_history.setOnClickListener({ v ->
            doNavigateToHistory()
        })
        btn_edit.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AuthencationActivity::class.java)
                it.startActivity(intent)
            }
        }
        ll_info.setOnClickListener {
            doNavigatietoDetail()
        }

        ll_cart.setOnClickListener {
            doNavigatetoCart()
        }
    }

    fun doNavigateToHistory() {
        findNavController().navigate(R.id.action_profileFragment_to_orderHistoryFragment)
    }

    fun doNavigatietoDetail() {
        var bundle = Bundle()
        bundle.putBoolean("STAFF", false)
        findNavController().navigate(R.id.action_profileFragment_to_detailProfileFragment, bundle)
    }

    fun doNavigatetoCart() {
        var bundle = Bundle()
        bundle.putInt("Product", 0)
        findNavController().navigate(R.id.action_profileFragment_to_cardFragment2,bundle)
    }

    fun setUpLayout() {
        if (preference.getValueBoolien(Constant.IS_LOGIN, false) == true) {
            btn_edit.visibility = View.GONE
            ll_info.visibility = View.VISIBLE
            context?.let {
                ShopRepository.getUser(it, preference.getValueInt(Constant.USER_ID))!!
                    .observe(viewLifecycleOwner,
                        Observer {
                            txt_acc_name.text = it.TenDangNhap
                            txt_acc_mail.text = it.email
                            txt_acc_phone.text = it.SDT
                            txt_acc_address.text = it.DiaChi_SoNha
                            if (!it.HinhAnh.isEmpty()) {
                                Glide.with(requireContext()).load(it.HinhAnh)
                                    .into(circularImageView)
                            }
                        })
            }
            ll_cart.visibility = View.VISIBLE
        } else {
            btn_edit.visibility = View.VISIBLE
            ll_info.visibility = View.GONE
            ll_cart.visibility = View.GONE
        }
    }

}
