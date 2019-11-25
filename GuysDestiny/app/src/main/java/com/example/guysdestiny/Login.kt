package com.example.guysdestiny

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProviders;
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.room.WifiListRequest

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {
    private lateinit var viewModel: UserViewModel
    val apiClient = APIClient()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)
        val testBtn: Button = view.findViewById(R.id.testBtn)
        val gotoSignUpButton: TextView = view.findViewById(R.id.gotoSignBtn)

        loginBtn.setOnClickListener { loginUser(activity!!.applicationContext) }
        testBtn.setOnClickListener { getWifiList(activity!!.applicationContext) }
        gotoSignUpButton.setOnClickListener { signFragment(view) }

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return view
    }


    private fun loginUser(context: Context) {
        var request: LoginRequest =
            LoginRequest()
        request.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
//        request.name = "testusessr"
//        request.password = "heslo123"
        request.name = "testicek"
        request.password = "heslo123"
        apiClient.loginUser(request)

//        Toast.makeText(context,"Hello Javatpoint", Toast.LENGTH_SHORT).show()
    }

    private fun getWifiList(context: Context) {
        var request: WifiListRequest =
            WifiListRequest()
        request.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
        request.uid = "145"

        apiClient.getRoomList(request)

//        Toast.makeText(context,"Hello Javatpoint", Toast.LENGTH_SHORT).show()
    }

    private fun signFragment(view: View) {
        view.findNavController().navigate(R.id.action_login_to_signUp)
    }
}
