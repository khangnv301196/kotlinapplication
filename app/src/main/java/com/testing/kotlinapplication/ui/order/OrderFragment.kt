package com.testing.kotlinapplication.ui.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.UserModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment() {

    private lateinit var btn_proceed: LinearLayout
    private lateinit var model: LiveData<UserModel>
    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_order, container, false)
        preference = Preference(itemView.context)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapping(view)
        model = ShopRepository.getUser(view.context, preference.getValueInt(Constant.USER_ID))!!
        model.observe(viewLifecycleOwner, Observer {
            txtinp_name.editText?.setText(it.TenDangNhap)
            txtinp_email.editText?.setText(it.email)
            txtinp_phone.editText?.setText(it.SDT)
            txtinp_city.editText?.setText(it.DiaChi_ThanhPho)
            txtinp_district.editText?.setText(it.DiaChi_Quan)
            txtinp_address.editText?.setText(it.DiaChi_SoNha)
        })
    }

    fun mapping(view: View) {
        btn_proceed = view.findViewById(R.id.btn_proceed)
        btn_proceed.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", txtinp_name.editText?.text.toString())
            bundle.putString("phone", txtinp_phone.editText?.text.toString())
            bundle.putString("city", txtinp_city.editText?.text.toString())
            bundle.putString("district", txtinp_district.editText?.text.toString())
            bundle.putString("address", txtinp_address.editText?.text.toString())
            findNavController().navigate(R.id.orderCompleteFragment, bundle)
        }
    }


}