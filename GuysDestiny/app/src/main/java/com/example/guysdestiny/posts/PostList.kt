package com.example.guysdestiny.posts


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.guysdestiny.R
import com.example.guysdestiny.UserViewModel
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.room.*
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.wifi.CustomAdapter
import com.example.guysdestiny.wifi.WifiData
import com.giphy.sdk.analytics.GiphyPingbacks
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.GiphyCoreUI
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.fragment_wifi_list.*
import kotlinx.android.synthetic.main.new_post_field.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class PostList : Fragment() {
    var posts = ArrayList<PostModel>()
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelData: LoginResponse
    private lateinit var roomId: String
    val apiClient = APIClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        viewModelData = viewModel.user.value!!

        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomId = arguments?.getString("wifiName").toString()
        if (!viewModel.currentWifi.equals(roomId) && !roomId.equals("public", ignoreCase = true)) {
            Toast.makeText(context, "Na tejto Wifi nie ste pripojeny!", Toast.LENGTH_LONG).show()
            new_post_field_id.visibility = View.GONE
        }

        getRoomList()
        setButtonsClickListeners(view)
    }


    fun fillPostListView() {
        recyclerView_posts?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapterPosts(posts)
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

        val request = ReadRequest()
        request.uid = viewModelData.uid
        request.room = roomId

        val call: Call<List<ReadResponse>> =
            apiClient.prepareRetrofit(true, viewModelData.access).readWifiListMessages(request)
        call.enqueue(object : Callback<List<ReadResponse>> {
            override fun onFailure(call: Call<List<ReadResponse>>, t: Throwable) {
                Log.d("xx", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ReadResponse>>,
                response: Response<List<ReadResponse>>
            ) {
                if (response.body() != null) {
                    posts = ArrayList<PostModel>()
                    val res: List<ReadResponse> = response.body()!!

                    for (item in res) {
                        posts.add(
                            PostModel(
                                item.uid,
                                item.roomid,
                                item.message,
                                item.name,
                                item.time
                            )
                        )
                    }
                    posts.reverse()
                    fillPostListView()
                }
            }
        })
    }

    fun addFakePost(newPost: PostModel) {
        posts.add(0, newPost)
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
        addFakePost(PostModel(viewModelData.uid, roomId, message, "Me", "Now"))
        postRoomListMessages(messageReq, viewModelData.access)
        fillPostListView()
    }

    fun postRoomListMessages(request: MessageRequest, TOKEN: String) {

        val call: Call<ResponseBody> =
            apiClient.prepareRetrofit(true, TOKEN).postMessageWifiList(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())
            }
        })
    }

}
