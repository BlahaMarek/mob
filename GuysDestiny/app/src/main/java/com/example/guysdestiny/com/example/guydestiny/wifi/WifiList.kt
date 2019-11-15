package com.example.guysdestiny.com.example.guydestiny.wifi


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guysdestiny.R

/**
 * A simple [Fragment] subclass.
 */
class WifiList : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_wifi_list, container, false)

        val recyclerView = rootView.findViewById(R.id.recyclerView) as? RecyclerView
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        val wifis = ArrayList<WifiData>()
        wifis.add(WifiData("public"))
        wifis.add(WifiData("private1"))
        wifis.add(WifiData("private2"))
        wifis.add(WifiData("private3"))

        val adapter = CustomAdapter(wifis)
        if (recyclerView != null) {
            recyclerView.adapter = adapter
        }


        return rootView
    }
}
