package com.testing.kotlinapplication.ui.authencation.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.repository.action.ShopRepository
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkPassword(
                txt_password.editText?.text.toString(),
                txtx_confirm.editText?.text.toString()
            )
        ) {
            ShopRepository.insertData()
        }
    }

    fun checkPassword(password: String, confirmPassword: String): Boolean {
        return password.equals(confirmPassword)
    }

}