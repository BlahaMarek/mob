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
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import androidx.navigation.Navigation
import com.example.guysdestiny.services.apiModels.user.RefreshRequest


/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {
    var PREF_REFRESH = "refresh"
    var PREF_UID = "uid"
    lateinit var preferences: SharedPreferences
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = this.activity!!.getSharedPreferences(PREF_REFRESH, Context.MODE_PRIVATE)

        var refresh: String = preferences.getString(PREF_REFRESH, "")!!
        var uid: String = preferences.getString(PREF_UID, "")!!
        if (refresh.isNotBlank() && uid.isNotBlank()) {

            var refreshReq: RefreshRequest = RefreshRequest()
            refreshReq.uid = uid
            refreshReq.refresh = refresh

            refreshUser(refreshReq, view)
        }
    }


    private fun loginUser(context: Context, view: View) {
        var user = LoginResponse()
        var request = LoginRequest()
        request.name = "testiceka"
        request.password = "heslo123"

//        request.name = loginName.text.toString()
//        request.password = passwd.text.toString()
//
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
                    preferences.edit().putString(PREF_REFRESH, user.refresh).apply()
                    preferences.edit().putString(PREF_UID, user.uid).apply()
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
        view.findNavController().navigate(R.id.action_login_to_wifiList)
    }

    fun refreshUser(refresh: RefreshRequest, view: View) {

        val call: Call<LoginResponse> = apiClient.prepareRetrofit(false, "").userRefresh(refresh)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("user refreshed", response.code().toString())
                if ( response.body() != null) {
                    var user = LoginResponse()
                    user = response.body()!!
                    viewModel.setUser(user)
                    preferences.edit().putString(PREF_REFRESH, user.refresh).apply()
                    preferences.edit().putString(PREF_UID, user.uid).apply()
                    view.findNavController().navigate(R.id.action_login_to_wifiList)
                } else {
                    Toast.makeText(context,"Prihlasovacie udaje nie su spravne", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
