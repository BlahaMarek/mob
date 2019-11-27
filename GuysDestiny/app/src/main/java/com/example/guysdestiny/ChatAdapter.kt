package com.example.guysdestiny

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.receiver_chat_item_layout.view.*
import kotlinx.android.synthetic.main.sender_chat_item_layout.view.*
import kotlin.random.Random

class ChatAdapter(val context: Context) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messages: ArrayList<Message> = ArrayList()
    private lateinit var contactUid: String
    private val VIEW_TYPE_MY_MESSAGE = 1
    private val VIEW_TYPE_OTHER_MESSAGE = 2

    fun addMessage(message: Message){
        messages.add(message)
        notifyDataSetChanged()
    }

    fun getMessages():ArrayList<Message>{
        return messages
    }

    fun setContactUid(uid: String)
    {
        contactUid = uid
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val messUid = messages[position].uid
//        tu dorobit o akeho usra ide, zatial random sa priradi
        return if(messUid == contactUid) {
            VIEW_TYPE_OTHER_MESSAGE
        }
        else {
            VIEW_TYPE_MY_MESSAGE
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.get(position)

        holder.bind(message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
            SenderMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sender_chat_item_layout, parent, false)
            )
        } else {
            ReceiverMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.receiver_chat_item_layout, parent, false)
            )
        }
    }

    inner class SenderMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtSenderMessage
        private var timeText: TextView = view.txtSenderMessageTime

        override fun bind(message: Message) {
            messageText.text = message.message
            timeText.text = message.time
        }
    }



    inner class ReceiverMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtReceiverMessage
        private var userText: TextView = view.txtReceiverUser
        private var timeText: TextView = view.txtReceiverMessageTime

        override fun bind(message: Message) {
            messageText.text = message.message
            userText.text = message.user
            timeText.text = message.time
        }
    }

    open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(message:Message) {}
    }

}


