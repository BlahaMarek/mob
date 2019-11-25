package com.example.guysdestiny


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_contact_list.*

/**
 * A simple [Fragment] subclass.
 */
class ContactList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        Log.d("ssid", wifiManager.connectionInfo.ssid.toString())
//        Log.d("bssid", wifiManager.connectionInfo.bssid.toString())


        val contacts = ArrayList<ContactData>()
        contacts.add(ContactData("Marek", 1))
        contacts.add(ContactData("Marcel", 2))
        contacts.add(ContactData("Karol", 3))
        contacts.add(ContactData("Nika", 4))
        contacts.add(ContactData("Marek", 5))
        contacts.add(ContactData("Marcel", 6))
        contacts.add(ContactData("Karol", 7))
        contacts.add(ContactData("Nika", 8))
        contacts.add(ContactData("Marek", 9))
        contacts.add(ContactData("Marcel", 10))
        contacts.add(ContactData("Karol", 11))
        contacts.add(ContactData("Nika", 12))
        contacts.add(ContactData("Marek", 13))
        contacts.add(ContactData("Marcel", 14))
        contacts.add(ContactData("Karol", 15))
        contacts.add(ContactData("Nika", 16))
        contacts.add(ContactData("Marek", 17))
        contacts.add(ContactData("Marcel", 18))
        contacts.add(ContactData("Karol", 19))
        contacts.add(ContactData("Nika", 20))

        super.onViewCreated(view, savedInstanceState)
        rv_contact_list?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ContactAdapter(contacts)
        }
    }
}
