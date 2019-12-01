package com.example.guysdestiny

import android.content.Context
import android.content.Intent
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
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.apiModels.user.RefreshRequest

/**
 * A simple [Fragment] subclass.
 */


class Login : Fragment() {
    var PREF_NAME = "guysdestiny"
    var PREF_REFRESH = "refresh"
    var PREF_ACCESS = "access"
    var PREF_UID = "uid"
    lateinit var preferences: SharedPreferences
    lateinit var loginName: EditText
    lateinit var passwd: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)
        val gotoSignUpButton: TextView = view.findViewById(R.id.gotoSignBtn)

        loginName = view.findViewById(R.id.loginText)
        passwd = view.findViewById(R.id.passwordText)

        loginBtn.setOnClickListener { loginUser(activity!!.applicationContext, view) }
        gotoSignUpButton.setOnClickListener { signFragment(view) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(
            true
            /** true means that the callback is enabled */
        ) {
            override fun handleOnBackPressed() {
                Toast.makeText(context, "Back button", Toast.LENGTH_SHORT).show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        preferences = this.activity!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var refresh: String = preferences.getString(PREF_REFRESH, "")!!
        var uid: String = preferences.getString(PREF_UID, "")!!
        if (refresh.isNotBlank() && uid.isNotBlank()) {

            var refreshReq: RefreshRequest = RefreshRequest()
            refreshReq.uid = uid
            refreshReq.refresh = refresh

            refreshUser(refreshReq, view, activity!!.applicationContext)
        }
    }


    private fun loginUser(context: Context, view: View) {
//        var user = LoginResponse()
        var request = LoginRequest()
        request.name = "testiceka"
        request.password = "heslo123"

        request.name = loginName.text.toString()
        request.password = passwd.text.toString()
//
//        if (loginName.text.isBlank() || passwd.text.isBlank()) {
//            Toast.makeText(context,"Vyplnte prihlasovacie udaje", Toast.LENGTH_SHORT).show()
//            return
//        }

        val call: Call<LoginResponse> = APIService.create(context).userLogin(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Zadali ste chujovske prihlasovacie udaje",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body() != null) {
                    preferences.edit().putString(PREF_REFRESH, response.body()!!.refresh).apply()
                    preferences.edit().putString(PREF_UID, response.body()!!.uid).apply()
                    preferences.edit().putString(PREF_ACCESS, response.body()!!.access).apply()
                    val inputManager =
                        activity!!.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )

                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("userUid", response.body()!!.uid)
                    intent.putExtra("userAccess", response.body()!!.access)
                    intent.putExtra("userRefresh", response.body()!!.refresh)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        context,
                        "Prihlasovacie udaje nie su spravne",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        })
    }

    private fun signFragment(view: View) {
        view.findNavController().navigate(R.id.action_login_to_signUp)
    }

    fun refreshUser(refresh: RefreshRequest, view: View, context: Context) {

        val call: Call<LoginResponse> = APIService.create(context).userRefresh(refresh)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body() != null) {
                    var user = LoginResponse()
                    user = response.body()!!
                    preferences.edit().putString(PREF_REFRESH, user.refresh).apply()
                    preferences.edit().putString(PREF_UID, user.uid).apply()
                    preferences.edit().putString(PREF_ACCESS, user.access).apply()

                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("userUid", user.uid)
                    intent.putExtra("userAccess", user.access)
                    intent.putExtra("userRefresh", user.refresh)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        context,
                        "Prihlasovacie udaje nie su spravne",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
