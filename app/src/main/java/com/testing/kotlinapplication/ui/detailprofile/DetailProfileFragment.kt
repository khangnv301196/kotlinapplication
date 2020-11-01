package com.testing.kotlinapplication.ui.detailprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.testing.kotlinapplication.MainActivity

import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.RegisterRespone
import com.testing.kotlinapplication.repository.UserModel
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.staff.StaffActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_profile.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProfileFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var preference: Preference
    private lateinit var model: LiveData<UserModel>

    private var isStaff = false

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
        model =
            ShopRepository.getUser(mContext, Preference(mContext).getValueInt(Constant.USER_ID))!!
        model.observe(viewLifecycleOwner, Observer {
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

        btn_logout.setOnClickListener {
            doLogout()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isStaff = arguments?.getBoolean("STAFF", false)!!
        if (isStaff == true) {
            (activity as StaffActivity)?.hideBottomNavigation()
        } else {
            (activity as MainActivity)?.hideBottomNavigation()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (isStaff == true) {
            (activity as StaffActivity)?.showBottomNavigation()
        } else {
            (activity as MainActivity)?.showBottomNavigation()
        }

    }

    fun doLogout() {
        doLogoutAPI(object : DataCallBack<RegisterRespone> {
            override fun Complete(respon: RegisterRespone) {
                if (model.hasActiveObservers()) {
                    model.removeObservers(viewLifecycleOwner)
                }
                //delete user
                ShopRepository.doDeleteUserByID(mContext, preference.getValueInt(Constant.USER_ID))
                preference.clearSharedPreference()
                var intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                if (isStaff == true) {
                    (activity as StaffActivity).startActivity(intent)
                    (activity as StaffActivity).finish()
                } else {
                    (activity as MainActivity).startActivity(intent)
                    (activity as MainActivity).finish()
                }

            }

            override fun Error(error: Throwable) {
                var intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                if (isStaff == true) {
                    (activity as StaffActivity).startActivity(intent)
                    (activity as StaffActivity).finish()
                } else {
                    (activity as MainActivity).startActivity(intent)
                    (activity as MainActivity).finish()
                }
            }

        })

    }

    fun doLogoutAPI(callback: DataCallBack<RegisterRespone>) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService().logout(preference.getValueString(Constant.TOKEN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("DetailProfileFragment", "logout")
                    callback.Complete(it)
                }, {
                    Log.d("DetailProfileFragment", "error")
                    callback.Error(it)
                })
        )
    }
}
