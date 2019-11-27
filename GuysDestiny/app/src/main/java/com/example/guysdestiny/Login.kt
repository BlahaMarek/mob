package com.example.guysdestiny

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProviders;
import com.example.guysdestiny.models.User
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.contact.ContactMessageRequest
import com.example.guysdestiny.services.apiModels.contact.ContactReadRequest
import com.example.guysdestiny.services.apiModels.room.ReadRequest
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.room.WifiListRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.RefreshRequest
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {
    lateinit var viewModel: UserViewModel
    lateinit var loginName: EditText
    lateinit var passwd: EditText
    val apiClient = APIClient()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)
        val gotoSignUpButton: TextView = view.findViewById(R.id.gotoSignBtn)

        loginName = view.findViewById(R.id.loginText)
        passwd = view.findViewById(R.id.passwordText)

        loginBtn.setOnClickListener { loginUser(activity!!.applicationContext, view) }
        gotoSignUpButton.setOnClickListener { signFragment(view) }

        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        return view
    }

    private fun loginUser(context: Context, view: View) {
        var user = LoginResponse()
        var request = LoginRequest()
        request.name = "testiceka"
        request.password = "heslo123"

        request.name = loginName.text.toString()
        request.password = passwd.text.toString()

//        if (loginName.text.isBlank() || passwd.text.isBlank()) {
//            Toast.makeText(context,"Vyplnte prihlasovacie udaje", Toast.LENGTH_SHORT).show()
//            return
//        }

        val call: Call<LoginResponse> = apiClient.prepareRetrofit(false, "").userLogin(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
                Toast.makeText(context,"Zadali ste chujovske prihlasovacie udaje", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if ( response.body() != null) {
                    user = response.body()!!
                    viewModel.setUser(user)
                    val inputManager =
                        activity!!.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    view.findNavController().navigate(R.id.action_login_to_wifiList)
                } else {
                    Toast.makeText(context,"Prihlasovacie udaje nie su spravne", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }


    private fun signFragment(view: View) {
        view.findNavController().navigate(R.id.action_login_to_signUp)
    }
}
