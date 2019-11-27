package com.example.guysdestiny.wifi

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
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.room.WifiListRequest
import com.example.guysdestiny.services.apiModels.room.WifiListResponse
import kotlinx.android.synthetic.main.fragment_wifi_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//ToDo:
//      1. Vytvorit ReadMe File OK
//      2. Ziskat SSID, BSSID  OK
//      3. Povolenie pre polohu OK
//      4. Vytvorit public room-ku  OK
//      5. Cez rest ziskat zvysne room-ky OK
//      6. Na onClickListener otvorit novy fragment + poslat data OK
//      ...
//      7. Obnovit wifimanagera pri zmene wifi
//      8. Animacia na onClick

class WifiList : Fragment() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission( activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        } else {
            val wifis = ArrayList<WifiData>()
            wifiMan(wifis)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun wifiMan(wifis: ArrayList<WifiData>) {
        wifis.add(WifiData("Public", "XsTDHS3C2YneVmEW5Ry7"))

        val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if ( wifiManager.connectionInfo.ssid != null) {
            val wifiSSID = wifiManager.connectionInfo.ssid.toString().replace("\"", "")
            wifis.add(WifiData(wifiSSID, wifiSSID))
            Log.d("ssid", wifiSSID)
        } else {
            val wifiBSSID = wifiManager.connectionInfo.bssid.toString()
            wifis.add(WifiData(wifiBSSID, wifiBSSID))
            Log.d("bssid", wifiBSSID)
        }
        getRoomList(wifis)
    }

    fun getRoomList(wifis: ArrayList<WifiData>) {
        val apiClient = APIClient()
        val request = WifiListRequest()
        request.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
        request.uid = "145"

        val call: Call<List<WifiListResponse>> = apiClient.prepareRetrofit(true).getWifiList(request)
        call.enqueue(object : Callback<List<WifiListResponse>> {
            override fun onFailure(call: Call<List<WifiListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<List<WifiListResponse>>, response: Response<List<WifiListResponse>>) {
                val res: List<WifiListResponse> = response.body()!!

                for(item in res){
                    wifis.add(WifiData(item.roomid, item.roomid))
                }

                recyclerView_wifiList?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = CustomAdapter(wifis)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("perDone", "permission for Location")
                    val wifis = ArrayList<WifiData>()
                    wifiMan(wifis)
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
