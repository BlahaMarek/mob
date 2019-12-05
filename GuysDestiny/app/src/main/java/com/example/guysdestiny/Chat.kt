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
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.apiModels.contact.*
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.UserFidRequest
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.GiphyCoreUI
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import okhttp3.ResponseBody
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

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        viewModelData = viewModel.user.value!!

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messAdapter = ChatAdapter(activity!!.applicationContext)

        contactUid = arguments?.getString("contactUid")!!
        messAdapter.setContactUid(contactUid)
        getContactListMessages()

        btnSend.setOnClickListener {

            if(txtMessage.text.isNotEmpty()) {
                sendMessage(txtMessage.text.toString())
            }
        }

        bt_addGif_chat.setOnClickListener{
            initGiphy()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun sendMessage(text: String){
        val sdf = SimpleDateFormat("yyyy-dd-M hh:mm:ss")
        val currentDate = sdf.format(Date())
        val message = Message(
            "ja",
            text,
            currentDate,
            viewModelData.uid
        )
        postContactListMessages(text)
        messAdapter.addMessage(message)
        resetInput()
        messageList.smoothScrollToPosition(messAdapter.getMessages().size - 1)
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

    fun getContactListMessages() {
        val contactReadRequest = ContactReadRequest()
        contactReadRequest.contact = contactUid
        contactReadRequest.uid = viewModelData.uid
        val call: Call<List<ContactReadResponse>> = APIService.create(activity!!.applicationContext).readContactListMessages(contactReadRequest)

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
                if(res.count() > 0){
                    setContactUid(res.get(0))
                }


                messageList?.apply {
                    layoutManager = LinearLayoutManager(context).apply {
                        stackFromEnd = true
                        reverseLayout = false
                    }
                    adapter = messAdapter
                }

            }
        })
    }

    fun setContactUid(message: ContactReadResponse){
        return
        if(viewModel.user.value!!.uid == message.uid){
            viewModel.setUserToWriteFID(message.contact_fid)
            return
        }

        viewModel.setUserToWriteFID(message.uid_fid)
    }

    fun postContactListMessages(message: String) {
        val contactMessageRequest = ContactMessageRequest()
        contactMessageRequest.contact = contactUid
        contactMessageRequest.message = message
        contactMessageRequest.uid = viewModelData.uid
        val call: Call<ResponseBody> = APIService.create(activity!!.applicationContext).postMessageContactList(contactMessageRequest)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())

                var req = UserFidRequest()
//                req.fid = viewModel.userToWriteFID.value!!
//                req.fid = "cgzwRUxhVMw:APA91bHUzd3au-wwXeQQqiFc97AfVrfmTX1tUQBf3W_qrhXfuEip-EZ4XMEfwVR9DfcCQTzFL-kNZdCg3t-AvJHejmXcQdwG3RqGI4u6acCza3S2loAJhTjweo7mqPs6P8HvEHiUvxhg"
//                req.uid = viewModelData.uid
//
//                val notify: Call<ResponseBody> = APIService.create(activity!!.applicationContext).userFid(req)
//
//                notify.enqueue(object : Callback<ResponseBody> {
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        Log.d("badRequest", t.message.toString())
//                    }
//                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                        Log.d("notify send", response.code().toString())
//                    }
//                    })
            }
        })

    }

    fun initGiphy() {
        var giphUrl: String
        context?.let { GiphyCoreUI.configure(it, "SG4rKVNYS5I9ISSQyOww9Mro0JdhK4KZ") }

        var settings =
            GPHSettings(gridType = GridType.waterfall, theme = LightTheme, dimBackground = true)
        val gifsDialog = GiphyDialogFragment.newInstance(settings)

        gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun onGifSelected(media: Media) {
                giphUrl = "gif:" + media.id
                sendMessage(giphUrl)
            }

            override fun onDismissed() {}
        }

        gifsDialog.show(activity!!.supportFragmentManager, "shit")
    }

}
