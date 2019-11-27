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
import com.example.guysdestiny.services.apiModels.contact.ContactReadRequest

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
        val gotoSignUpButton: TextView = view.findViewById(R.id.gotoSignBtn)

        loginBtn.setOnClickListener { loginUser(activity!!.applicationContext) }
        gotoSignUpButton.setOnClickListener { signFragment(view) }

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return view
    }


    private fun loginUser(context: Context) {
//        var request: LoginRequest = LoginRequest()
//        request.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
//        request.name = "testiceka"
//        request.password = "heslo123"
//        apiClient.createUser(request)

//        var refresh: RefreshRequest = RefreshRequest()
//        refresh.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
//        refresh.refresh = "530837aa4d2b2ac54da8df2d07b109d46f8c1165"
//        refresh.uid = "145"
//        apiClient.refreshUser(refresh)

//        var readWifi: ReadRequest = ReadRequest()
//        readWifi.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
//        readWifi.room = "aa"
//        readWifi.uid = "145"
//        apiClient.getRoomListMessages(readWifi)

//        var contactList: ContactListRequest = ContactListRequest()
//        contactList.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
//        contactList.uid = "145"
//        apiClient.getContactList(contactList)

        var contactRead: ContactReadRequest = ContactReadRequest()
        contactRead.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
        contactRead.uid = "145"
        contactRead.contact = "7"
        apiClient.getContactListMessages(contactRead)


//        Toast.makeText(context,"Hello Javatpoint", Toast.LENGTH_SHORT).show()
    }

    private fun signFragment(view: View) {
        view.findNavController().navigate(R.id.action_login_to_signUp)
    }
}
