package com.example.guysdestiny.posts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guysdestiny.R
import com.example.guysdestiny.services.apiModels.room.ReadResponse
import com.giphy.sdk.analytics.GiphyPingbacks
import com.giphy.sdk.analytics.GiphyPingbacks.context
import kotlinx.android.synthetic.main.fragment_post_list.*
import java.util.regex.Pattern


class CustomAdapterPosts(val posts: ArrayList<ReadResponse>, val userUid: String) :
    RecyclerView.Adapter<CustomAdapterPosts.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: ReadResponse = posts[position]
        holder.tvAuthor.text = post.name + ",  "
        holder.tvTime.text = post.time
        holder.tvText.text = post.message

        holder.tvAuthor.setOnClickListener {
            if(!userUid.equals(post.uid)){
                val bundle = bundleOf("contactUid" to post.uid)
                holder.itemView.findNavController().navigate(R.id.action_postList_to_chat, bundle)
            }else{
                Toast.makeText(holder.thisC, "It is u! u cant write u!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_field, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        var message: String = "" + holder.tvText.text
        if (isGif(message)) {
            holder.tvText.visibility = View.GONE
            holder.gifView.visibility = View.VISIBLE
            Glide.with(holder.thisC).asGif().load(formatUrl(message)).into(holder.gifView);
        } else {
            holder.gifView.visibility = View.GONE
            holder.tvText.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun formatUrl(message: String): String {
        return "https://media.giphy.com/media/" + message.replace("gif:", "") + "/giphy.gif"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText = itemView.findViewById(R.id.tv_text) as TextView
        val tvAuthor = itemView.findViewById(R.id.tv_author) as TextView
        val tvTime = itemView.findViewById(R.id.tv_time) as TextView
        val gifView = itemView.findViewById(R.id.iv_gif_view) as ImageView
        val thisC = itemView.context
    }

    fun isGif(message: String): Boolean {
        return message.startsWith("gif:")
    }

}