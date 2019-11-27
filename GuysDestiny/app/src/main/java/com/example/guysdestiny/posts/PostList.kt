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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.guysdestiny.R
import com.giphy.sdk.analytics.GiphyPingbacks
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.GiphyCoreUI
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.new_post_field.*

/**
 * A simple [Fragment] subclass.
 */
class PostList : Fragment() {
    val posts = ArrayList<PostModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFakeData()

        fillPostListView()

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
                addFakePost(PostModel("1", "xxx", et_newMessage?.text.toString(), "jozo", "2004"))
                fillPostListView()
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

    fun addFakePost(newPost: PostModel) {
        posts.add(0, newPost)
    }

    fun initFakeData() {
        addFakePost(PostModel("1", "xxx", "Ahoooooooj", "jozo", "2004"))
        addFakePost(PostModel("1", "xxx", "xxxx", "jozo", "2004"))
        addFakePost(
            PostModel(
                "1",
                "xxx",
                "https://media.giphy.com/media/WUrKpJUmNAsxTzHhao/giphy.gif",
                "jozo",
                "2004"
            )
        )
    }

    fun initGiphy() {
        var giphUrl: String
        context?.let { GiphyCoreUI.configure(it, "SG4rKVNYS5I9ISSQyOww9Mro0JdhK4KZ") }

        var settings =
            GPHSettings(gridType = GridType.waterfall, theme = LightTheme, dimBackground = true)
        val gifsDialog = GiphyDialogFragment.newInstance(settings)

        gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun onGifSelected(media: Media) {
                giphUrl = "https://media.giphy.com/media/" + media.id + "/giphy.gif"
                addFakePost(PostModel("1", "xxx", giphUrl, "jozo", "2004"))
                fillPostListView()
            }
            override fun onDismissed() {}
        }

        gifsDialog.show(activity!!.supportFragmentManager, "shit")
    }
}
