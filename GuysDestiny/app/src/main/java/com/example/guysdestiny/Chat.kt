package com.example.guysdestiny


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.apiModels.contact.ContactListRequest
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse
import com.example.guysdestiny.services.apiModels.contact.ContactReadRequest
import com.example.guysdestiny.services.apiModels.contact.ContactReadResponse
import com.example.guysdestiny.services.apiModels.room.ReadRequest
import com.example.guysdestiny.services.apiModels.room.ReadResponse
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import kotlinx.android.synthetic.main.fragment_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Chat : Fragment() {

    private lateinit var messAdapter: ChatAdapter
    private lateinit var contactUid: String
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelData: LoginResponse

    val apiClient = APIClient()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        viewModelData = viewModel.user.value!!

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messAdapter = ChatAdapter(activity!!.applicationContext)
        val contactReadRequest = ContactReadRequest()
        contactUid = arguments?.getString("contactUid")!!
        messAdapter.setContactUid(contactUid)
        contactReadRequest.contact = contactUid
        contactReadRequest.uid = viewModelData.uid
        getContactListMessages(contactReadRequest)

        btnSend.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-dd-M hh:mm:ss")
            val currentDate = sdf.format(Date())
            if(txtMessage.text.isNotEmpty()) {
                val message = Message(
                    "HenoUser",
                    txtMessage.text.toString(),
                    currentDate,
                    "256"
                )
                messAdapter.addMessage(message)
                resetInput()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

     fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        val inputManager =
            activity!!.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun getContactListMessages(request: ContactReadRequest) {

        val call: Call<List<ContactReadResponse>> = apiClient.prepareRetrofit(true, viewModelData.access ).readContactListMessages(request)

        call.enqueue(object : Callback<List<ContactReadResponse>> {
            override fun onFailure(call: Call<List<ContactReadResponse>>, t: Throwable) {

                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ContactReadResponse>>,
                response: Response<List<ContactReadResponse>>
            ) {
                Log.d("response", response.message())
               val res: List<ContactReadResponse> = response.body()!!
                for(item in res)
                {
                    messAdapter.addMessage(Message(item.uid_name, item.message, item.time, item.uid))
                }
                messageList?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = messAdapter
                }
            }
        })
    }
}
