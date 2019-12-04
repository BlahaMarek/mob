package com.example.guysdestiny


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.localDatabase.ContactDatabaseService
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.ConnectionService
import com.example.guysdestiny.services.apiModels.contact.ContactListRequest
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import kotlinx.android.synthetic.main.fragment_contact_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ContactList : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelData: LoginResponse
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!
        viewModelData = viewModel.user.value!!

        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var contacts = ArrayList<ContactListResponse>()

        if (!viewModel.contactList.value.isNullOrEmpty()) {
            contacts = ArrayList(viewModel.contactList.value!!)
            rv_contact_list?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ContactAdapter(contacts)
            }
        }
        else {
            getContactList(contacts)
        }

        btnWifiList.setOnClickListener {
            view.findNavController().navigate(R.id.wifiList)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun getContactList(contacts: ArrayList<ContactListResponse>) {
        val dbHandler = ContactDatabaseService(activity!!.applicationContext)
        if(!ConnectionService().isConnectedToNetwork(activity!!.applicationContext))
        {
            Toast.makeText(
                context,
                "Ne ste pripojený k internetu, preto všetky údaje nemusia byť aktuálne",
                Toast.LENGTH_SHORT
            ).show()
            val contactsFromLocalDb = dbHandler.getContacts()
            for(item in contactsFromLocalDb){
                contacts.add(item)
            }
            viewModel.setContactList(contactsFromLocalDb)
            rv_contact_list?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ContactAdapter(contacts)
            }
            return
        }
        val contactRequest = ContactListRequest()
        contactRequest.uid = viewModelData.uid
        val call: Call<List<ContactListResponse>> = APIService.create(activity!!.applicationContext).getContactList(contactRequest)

        call.enqueue(object : Callback<List<ContactListResponse>> {
            override fun onFailure(call: Call<List<ContactListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ContactListResponse>>,
                response: Response<List<ContactListResponse>>
            ) {
                val res: List<ContactListResponse> = response.body()!!

                if(res.count() > 0)
                {
                    dbHandler.addContacts(res)
                }

                for(item in res){
                    contacts.add(ContactListResponse(item.id, item.name))
                }
                viewModel.setContactList(contacts)
                rv_contact_list?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = ContactAdapter(contacts)
                }
            }
        })
    }
}
