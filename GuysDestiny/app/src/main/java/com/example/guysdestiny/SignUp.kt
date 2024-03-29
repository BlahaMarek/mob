package com.example.guysdestiny


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.guysdestiny.localDatabase.UserDatabaseService
import com.example.guysdestiny.models.User
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SignUp : Fragment() {
    lateinit var loginName: EditText
    lateinit var passwd: EditText
    lateinit var passwdConfirm: EditText

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signBtn: Button = view.findViewById(R.id.signBtn)
        val gotoLoginButton: TextView = view.findViewById(R.id.gotoLoginBtn)

        loginName = view.findViewById(R.id.loginText)
        passwd = view.findViewById(R.id.passwordText)
        passwdConfirm = view.findViewById(R.id.passwordText2)

        signBtn.setOnClickListener { createUser(activity!!.applicationContext, view) }
        gotoLoginButton.setOnClickListener { loginFragment(view) }

        return view
    }

    private fun createUser(context: Context, view: View) {
        if (loginName.text.isBlank() || passwd.text.isBlank() || passwdConfirm.text.isBlank()) {
            Toast.makeText(context,"Vyplnte registracne udaje", Toast.LENGTH_SHORT).show()
            return
        }

        if (loginName.text.length < 5) {
            Toast.makeText(context,"Login musi mat minimalne 5 znakov", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwd.text.length < 8 || passwdConfirm.text.length < 8) {
            Toast.makeText(context,"Heslo musi mat minimalne 8 znakov", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwd.text.toString().equals(passwdConfirm.text.toString())) {
            var request = LoginRequest()
            request.name = loginName.text.toString()
            request.password = passwd.text.toString()

            val call: Call<LoginResponse> = APIService.create(context).userCreate(request)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context,"Registracia sa nepodarila", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if ( response.body() != null) {

                        //ulozenie usera do SQLite
                        val dbHandler = UserDatabaseService(context)
                        val userToSave = User()
                        userToSave.uid = response.body()!!.uid
                        userToSave.access = response.body()!!.access
                        userToSave.refresh = response.body()!!.refresh
                        val dbResult = dbHandler.addUser(userToSave)
                        if(dbResult.toInt() == -1)
                        {
                            dbHandler.updateUser(userToSave)
                        }

                        loginFragment(view)

                    } else {
                        if (response.code().equals(500)) {
                            Toast.makeText(context,"Zadany login uz existuje", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(context,"Nieco sa posralo", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })
        }
        else {
            Toast.makeText(context,"Zadane hesla sa nezhoduju", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginFragment(view: View) {
        view.findNavController().navigate(R.id.action_signUp_to_login)
    }
}
