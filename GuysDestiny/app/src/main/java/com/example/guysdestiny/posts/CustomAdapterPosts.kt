package com.example.guysdestiny.posts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guysdestiny.R
import com.giphy.sdk.analytics.GiphyPingbacks
import com.giphy.sdk.analytics.GiphyPingbacks.context
import kotlinx.android.synthetic.main.fragment_post_list.*
import java.util.regex.Pattern


class CustomAdapterPosts(val posts: ArrayList<PostModel>) :
    RecyclerView.Adapter<CustomAdapterPosts.ViewHolder>() {

    var URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: PostModel = posts[position]
        holder.tvAuthor.text = post.name + ", "
        if(isUrl(post.message)){
            Glide.with(holder.thisC).asGif().load(post.message).into(holder.gifView);
        }else {
            holder.tvText.text = post.message
        }

        holder.postLayout.setOnClickListener {
            Log.d("xxx", "klikol si $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_field, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText = itemView.findViewById(R.id.tv_text) as TextView
        val tvAuthor = itemView.findViewById(R.id.tv_author) as TextView
        val gifView = itemView.findViewById(R.id.iv_gif_view) as ImageView
        val postLayout = itemView.findViewById(R.id.lt_post) as View
        val thisC = itemView.context
    }

    fun isUrl(message: String): Boolean {
        val p = Pattern.compile(URL_REGEX)
        val m = p.matcher(message)
        return m.find()
    }

}