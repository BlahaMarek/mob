package com.example.guysdestiny

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse

class ContactAdapter(val contactList: ArrayList<ContactListResponse>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        val contact: ContactListResponse = contactList[position]
        holder.textViewName.text = contact.name
        holder.card.setOnClickListener{
            Log.d("xxx", "klikol si ${contact.name}" )
            val bundle = bundleOf("contactUid" to contact.id)
            holder.itemView.findNavController().navigate(R.id.action_contactList_to_chat, bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.tv_contact_name) as TextView
        val card = itemView.findViewById(R.id.ll_contact_item) as View
    }
}