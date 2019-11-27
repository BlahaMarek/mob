package com.example.guysdestiny.wifi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.guysdestiny.R


class CustomAdapter(val wifiList: ArrayList<WifiData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wifi: WifiData = wifiList[position]
        holder.textViewName.text = wifi.name

        holder.card.setOnClickListener{
            Log.d("xxx", "klikol si ${wifi.name} s id ${wifi.id}" )
            val bundle = bundleOf("wifiName" to wifi.name)
            holder.itemView.findNavController().navigate(R.id.action_wifiList_to_postList, bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wifi_card_layout, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.wifiName) as TextView
        val card = itemView.findViewById(R.id.wifiLayout) as View
    }

}