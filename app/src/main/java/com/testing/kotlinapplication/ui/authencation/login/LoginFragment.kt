package com.testing.kotlinapplication.ui.authencation.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.ui.authencation.AuthencationActivity
import com.testing.kotlinapplication.ui.staff.StaffActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import com.testing.kotlinapplication.util.view.ProgressDialogutil
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var dialog: Dialog
    private lateinit var preference: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_login, container, false) as View
        preference = Preference(itemView.context)
        dialog = ProgressDialogutil.setProgressDialog(itemView.context, "loading...")
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AuthencationActivity).setUIGroup(view.findViewById(R.id.group))
        btn_login.setOnClickListener {
            showDialog(dialog)
            context?.let { it ->
                doLogin(
                    it,
                    txt_name.editText?.text.toString(),
                    textInputLayout.editText?.text.toString()
                )
            }
        }
        btn_register.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment();
            findNavController().navigate(action)
        }
    }

    fun doLogin(context: Context, mUserName: String, mPassword: String) {
//        var userModel =
//            ShopRepository.checkUser(context, mUserName)!!.observe(viewLifecycleOwner, Observer {
//                if (it == null) {
//                    Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show()
//                } else {
//                    var data = Base64.decode(it.Password, Base64.DEFAULT)
//                    var dbPassword = String(data)
//                    if (mPassword.equals(dbPassword)) {
//                        preference.save(Constant.IS_LOGIN, true)
//                        preference.save(Constant.USER_TYPE, it.userType)
//                    }
//                    hideDialog(dialog)
//                    if (preference.getValueInt(Constant.USER_TYPE) == 1) {
//                        var intent = Intent(context, MainActivity::class.java)
//                        intent.flags =
//                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        (activity as AuthencationActivity).startActivity(intent)
//                        (activity as AuthencationActivity).finish()
//                    } else if (preference.getValueInt(Constant.USER_TYPE) == 2) {
//                        var intent = Intent(context, StaffActivity::class.java)
//                        intent.flags =
//                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        (activity as AuthencationActivity).startActivity(intent)
//                        (activity as AuthencationActivity).finish()
//                    }
//                }
//            })
        val paramater = HashMap<String, String>()
        paramater.put("email", "khangnv@gmail.com")
        paramater.put("password", "123456")
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ServiceBuilder.buildService().login(paramater)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    hideDialog(dialog)
                    when (it.status) {
                        200 -> {
                            Toast.makeText(context, it.user.email, Toast.LENGTH_SHORT).show()
                            preference.save(Constant.TOKEN, "Bearer ${it.user.access_token}")
                            preference.save(Constant.IS_LOGIN, true)
                            var intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            (activity as AuthencationActivity).startActivity(intent)
                            (activity as AuthencationActivity).finish()

                        }
                        401 -> {
                            Toast.makeText(context, "Error 401", Toast.LENGTH_SHORT).show()
                        }
                        500 -> {
                            Toast.makeText(context, "Error 500", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, {
                    hideDialog(dialog)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                })
        )
    }

    fun showDialog(dialog: Dialog) {
        dialog.show()
    }

    fun hideDialog(dialog: Dialog) {
        dialog.hide()
    }

    fun doMappingUI(view: View) {

    }

}