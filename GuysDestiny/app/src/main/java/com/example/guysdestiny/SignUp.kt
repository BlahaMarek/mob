package com.example.guysdestiny


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.guysdestiny.services.APIClient

/**
 * A simple [Fragment] subclass.
 */
class SignUp : Fragment() {
    lateinit var viewModel: UserViewModel
    lateinit var loginName: EditText
    lateinit var passwd: EditText
    lateinit var passwdConfirm: EditText
    val apiClient = APIClient()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signBtn: Button = view.findViewById(R.id.signBtn)
        val gotoLoginButton: TextView = view.findViewById(R.id.gotoLoginBtn)

        loginName = view.findViewById(R.id.loginText)
        passwd = view.findViewById(R.id.passwordText)
        passwdConfirm = view.findViewById(R.id.passwordText2)

        signBtn.setOnClickListener { createUser(activity!!.applicationContext) }
        gotoLoginButton.setOnClickListener { loginFragment(view) }

        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        return view
    }

    private fun createUser(context: Context) {
        if (passwd.text.equals(passwdConfirm.text)) {
            // ak ukikatny tak pridaj
        }
        else {
            Toast.makeText(context,"Zadane hesla sa nezhoduju", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginFragment(view: View) {
        view.findNavController().navigate(R.id.action_signUp_to_login)
    }
}
