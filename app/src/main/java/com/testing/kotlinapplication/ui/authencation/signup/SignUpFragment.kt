package com.testing.kotlinapplication.ui.authencation.signup

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.util.util
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment() {

    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        dialog = ProgressDialogutil.setProgressDialog(itemView.context, "loading...")
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AuthencationActivity).setUIGroup(group)
        btn_sign_up.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                showDialog(dialog)
                if (doValidateForm(view)) {
                    if (checkPassword(
                            txt_password.editText?.text.toString(),
                            txtx_confirm.editText?.text.toString()
                        )
                    ) {
                        context?.let {
                            ShopRepository.insertData(
                                it,
                                txt_name_up.editText?.text.toString(),
                                txt_password.editText?.text.toString(),
                                txt_email.editText?.text.toString(),
                                txt_address.editText?.text.toString(),
                                2
                            )
                            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                                hideDialog(dialog)
                                (activity as AuthencationActivity).onBackPressed()
                            }, 3000)
                        }
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            hideDialog(dialog)
                            txt_password.error = "Your Password is not correct"
                        }, 3000)


                    }
                } else {
                    hideDialog(dialog)
                }

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
        return true

    }

    fun showDialog(dialog: Dialog) {
        dialog.show()
    }

    fun hideDialog(dialog: Dialog) {
        dialog.hide()
    }

}