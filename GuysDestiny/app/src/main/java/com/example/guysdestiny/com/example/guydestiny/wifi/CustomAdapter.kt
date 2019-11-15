package com.example.guysdestiny.com.example.guydestiny.wifi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guysdestiny.R

class CustomAdapter(val wifiList: ArrayList<WifiData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wifi: WifiData = wifiList[position]

        holder.textViewName.text = wifi.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wifi_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.wifiName) as TextView
    }
}