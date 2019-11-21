package com.example.guysdestiny.com.example.guydestiny.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.R
import kotlinx.android.synthetic.main.fragment_wifi_list.*



//ToDo:
//      1. Vytvorit ReadMe File OK
//      2. Ziskat SSID, BSSID  OK
//      3. Povolenie pre polohu OK
//      4. Vytvorit public room-ku  OK
//      ...
//      3. Cez rest ziskat zvysne room-ky
//      5. Na onClickListener otvorit novy fragment
//      6. Animacia na onClick

class WifiList : Fragment() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission( activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        } else {
            wifiMan()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun wifiMan() {
        val wifis = ArrayList<WifiData>()
        wifis.add(WifiData("Public", "XsTDHS3C2YneVmEW5Ry7"))

        val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if ( wifiManager.connectionInfo.ssid != null){
            val wifiSSID = wifiManager.connectionInfo.ssid.toString()
            wifis.add(WifiData(wifiSSID, wifiSSID))
            Log.d("ssid", wifiSSID)
        } else {
            val wifiBSSID = wifiManager.connectionInfo.bssid.toString()
            wifis.add(WifiData(wifiBSSID, wifiBSSID))
            Log.d("bssid", wifiBSSID)
        }

        wifis.add(WifiData("Private1", "null"))
        wifis.add(WifiData("Private2", "null"))
        wifis.add(WifiData("Private3", "null"))

        recyclerView_wifiList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapter(wifis)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("perDone", "permission for Location")
                    wifiMan()
                } else {
                    Log.d("eee", "no permission for Location")
                }
                return
            }
            else -> {
            }
        }
    }
}
