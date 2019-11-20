package com.example.guysdestiny.com.example.guydestiny.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.R
import kotlinx.android.synthetic.main.fragment_wifi_list.*


//ToDo:
//      1. Vytvorit ReadMe File
//      2. Ziskat SSID, BSSID
//      3. Cez rest ziskat zvysne room-ky
//      4. Vytvorit public room-ku
//      5. Na onClickListener otvorit novy fragment
//      6. Animacia na onClick

class WifiList : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        Log.d("ssid", wifiManager.connectionInfo.ssid.toString())
//        Log.d("bssid", wifiManager.connectionInfo.bssid.toString())


        val wifis = ArrayList<WifiData>()
        wifis.add(WifiData("Public"))
        wifis.add(WifiData("Private1"))
        wifis.add(WifiData("Private2"))
        wifis.add(WifiData("Private3"))

        super.onViewCreated(view, savedInstanceState)
        recyclerView_wifiList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapter(wifis)
        }
    }

}
