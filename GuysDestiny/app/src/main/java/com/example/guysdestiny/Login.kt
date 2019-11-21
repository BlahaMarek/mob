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
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {
    private lateinit var viewModel: UserViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)
        val gotoSignUpButton: TextView = view.findViewById(R.id.gotoSignBtn)

        loginBtn.setOnClickListener { loginUser(activity!!.applicationContext) }
        gotoSignUpButton.setOnClickListener { signFragment(view) }

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return view
    }


    private fun loginUser(context: Context) {
        Toast.makeText(context,"Hello Javatpoint", Toast.LENGTH_SHORT).show()
    }

    private fun signFragment(view: View) {
        view.findNavController().navigate(R.id.action_login_to_signUp)
    }
}
