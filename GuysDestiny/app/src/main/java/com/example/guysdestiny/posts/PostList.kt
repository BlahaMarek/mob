package com.example.guysdestiny.posts


import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.R
import com.example.guysdestiny.UserViewModel
import com.example.guysdestiny.localDatabase.PostDatabaseService
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.ConnectionService
import com.example.guysdestiny.services.MessagingService
import com.example.guysdestiny.services.apiModels.room.*
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.GiphyCoreUI
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.new_post_field.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class PostList : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelData: LoginResponse
    private lateinit var roomId: String
    lateinit var preferences: SharedPreferences
    var PREF_NAME = "guysdestiny"

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("xx", "chhhhh")
            getRoomList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        viewModelData = viewModel.user.value!!

        preferences = this.activity!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomId = arguments?.getString("wifiName").toString()
        if (roomId.equals("public", ignoreCase = true)) {
            roomId = "XsTDHS3C2YneVmEW5Ry7"
        }
        Log.d("vm", viewModel.currentWifi.value.toString())
        if (!viewModel.currentWifi.value.equals(roomId) && !roomId.equals("XsTDHS3C2YneVmEW5Ry7", ignoreCase = true)) {
            Toast.makeText(context, "Na tejto Wifi nie ste pripojeny!", Toast.LENGTH_LONG).show()
            new_post_field_id.visibility = View.GONE
        }

        getRoomList()
        setButtonsClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        view!!.context.registerReceiver(receiver, IntentFilter("POST_NOTIFICATION"))
        Log.d("ZACAL_POST", "SSSSSSSSSSSSSSSSSs")
    }

    override fun onPause() {
        super.onPause()
        view!!.context.unregisterReceiver(receiver)
        Log.d("SKONCIL_POST", "SSSSSSSSSSSSSSSSSs")
    }

    fun fillPostListView() {
        recyclerView_posts?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapterPosts(viewModel.roomRead.value!!, viewModel.user.value!!.uid)
        }
    }

    fun setButtonsClickListeners(view: View) {
        bt_addGif?.setOnClickListener {
            initGiphy()
        }
        bt_send_sessage?.setOnClickListener {
            if (et_newMessage?.text.toString().length > 0) {
                sendMessage(et_newMessage?.text.toString())
                et_newMessage?.setText("")
                et_newMessage?.clearFocus()
                view.hideKeyboard()
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getRoomList() {
        val dbHandler = PostDatabaseService(activity!!.applicationContext)
        //Ak pouzivatel nema pristup na internet zobrazujeme udaje z lokalnej DB a nevolame API yo servera
        if(!ConnectionService().isConnectedToNetwork(activity!!.applicationContext))
        {
            Toast.makeText(
                context,
                "Ne ste pripojený k internetu, preto všetky údaje nemusia byť aktuálne",
                Toast.LENGTH_SHORT
            ).show()
            val postsFromLocalDb = dbHandler.getPosts(roomId)
            viewModel.setRoomtRead(postsFromLocalDb)
            fillPostListView()
        }else{
            val request = ReadRequest()
            request.uid = viewModelData.uid
            request.room = roomId
            // pokial nepridu udaje zo servera, zobrazujeme data y lokalnej DB
            val postsFromLocalDb = dbHandler.getPosts(roomId)
            viewModel.setRoomtRead(postsFromLocalDb)
            fillPostListView()
            //////////////////////////////////////////////////////////////////////////////////////
            val call: Call<ArrayList<ReadResponse>> = APIService.create(activity!!.applicationContext).readWifiListMessages(request)
            call.enqueue(object : Callback<ArrayList<ReadResponse>> {
                override fun onFailure(call: Call<ArrayList<ReadResponse>>, t: Throwable) {
                    Log.d("xx", t.message.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<ReadResponse>>,
                    response: Response<ArrayList<ReadResponse>>
                ) {
                    if (response.body() != null) {
                        val res: ArrayList<ReadResponse> = response.body()!!
                        res.reverse()
                        dbHandler.addPosts(res)
                        viewModel.setRoomtRead(res)
                        fillPostListView()
                    }
                }
            })
        }
    }

    fun addFakePost(newPost: ReadResponse) {
//        posts.add(0, newPost)
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

    fun sendMessage(message: String){
        val messageReq: MessageRequest = MessageRequest()
        messageReq.message = message
        messageReq.room = roomId
        messageReq.uid = viewModelData.uid
//        Log.d("xx" ,message);
//        addFakePost(ReadResponse(viewModelData.uid, roomId, message, "Me", "Now"))
        postRoomListMessages(messageReq, viewModelData.access)
        var response = ReadResponse()
        response.message = message
        response.name = ""
        response.roomid = roomId
        response.time = "NOW"
        response.uid = viewModelData.uid
        viewModel.addRoomListPost(response)
        fillPostListView()
    }

    fun postRoomListMessages(request: MessageRequest, TOKEN: String) {

        val call: Call<ResponseBody> =APIService.create(activity!!.applicationContext).postMessageWifiList(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())
                val service = MessagingService()
                service.sendNotification(
                    "",
                    arguments?.getString("wifiName").toString(),
                    viewModelData.uid,
                    preferences.getString("login", "")!!
                )
//                getRoomList()
            }
        })
    }

}
