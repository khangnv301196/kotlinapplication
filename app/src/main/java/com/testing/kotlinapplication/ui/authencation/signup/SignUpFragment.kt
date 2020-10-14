package com.testing.kotlinapplication.ui.authencation.signup

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.util.util
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment() {

    private lateinit var dialog: Dialog
    private var gender = "Nam"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        dialog = ProgressDialogutil.setProgressDialog(itemView.context, "loading...")
        mapping(itemView)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AuthencationActivity).setUIGroup(group)
        btn_sign_up.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                doRegisterUseCase(view)
            }
        })


    }

    fun checkPassword(password: String, confirmPassword: String): Boolean {
        if (!password.isEmpty()) {
            return password.equals(confirmPassword)
        } else return false
    }

    fun doValidateForm(view: View): Boolean {
        view.txt_name_up.editText?.let { return util.isValidName(it, true) }
        view.txt_email.editText?.let { return util.isValidEmail(it, true) }
        view.txt_password.editText?.let { return util.isValidPassword(it, true) }
        view.txtx_confirm.editText?.let { return util.isValidPassword(it, true) }
        view.txt_city.editText?.let { return util.isValidName(it, true) }
        view.txt_ward.editText?.let { return util.isValidName(it, true) }
        view.txt_address.editText?.let { return util.isValidName(it, true) }
        return true

    }

    fun doRegisterUseCase(view: View) {
        showDialog(dialog)
        if (doValidateForm(view)) {
            if (checkPassword(
                    txt_password.editText?.text.toString(),
                    txtx_confirm.editText?.text.toString()
                )
            ) {
                var param = HashMap<String, String>()
                param.put("TenDangNhap", txt_name_up.editText?.text.toString())
                param.put("email", txt_email.editText?.text.toString())
                param.put("password", txt_password.editText?.text.toString())
                param.put("GioiTinh", gender)
                param.put("SDT", txt_phone.editText?.text.toString())
                param.put("DiaChi_ThanhPho", txt_city.editText?.text.toString())
                param.put("DiaChi_Quan", txt_ward.editText?.text.toString())
                param.put("DiaChi_SoNha", txt_address.editText?.text.toString())

                doRegisterToServer(param)
            } else {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    hideDialog(dialog)
                    txt_password.error = "Your Confirm Password is not correct"
                }, 3000)
            }
        } else {
            hideDialog(dialog)
        }
    }

    fun mapping(view: View) {
        view.male.isChecked = true
        view.male.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1 == true) {
                    gender = "Nam"
                } else {
                    gender = "Nữ"
                }
            }
        })
    }

    fun doRegisterToServer(param: HashMap<String, String>) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService().register(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    hideDialog(dialog)
                    (activity as AuthencationActivity).onBackPressed()
                }, {
                    hideDialog(dialog)
                    Toast.makeText(context, "Error when Register", Toast.LENGTH_SHORT).show()
                })
        )
    }

    fun showDialog(dialog: Dialog) {
        dialog.show()
    }

    fun hideDialog(dialog: Dialog) {
        dialog.hide()
    }

}