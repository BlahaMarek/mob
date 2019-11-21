package com.example.guysdestiny


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

/**
 * A simple [Fragment] subclass.
 */
class SignUp : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signBtn: Button = view.findViewById(R.id.signBtn)
        val gotoLoginButton: TextView = view.findViewById(R.id.gotoLoginBtn)

        signBtn.setOnClickListener { createUser(activity!!.applicationContext) }
        gotoLoginButton.setOnClickListener { loginFragment(view) }

        return view
    }

    private fun createUser(context: Context) {
        Toast.makeText(context,"Hello Javatpoint",Toast.LENGTH_SHORT).show()
    }

    private fun loginFragment(view: View) {
        view.findNavController().navigate(R.id.action_signUp_to_login)
    }
}
