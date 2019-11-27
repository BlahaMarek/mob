package com.example.guysdestiny


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.services.APIClient
import com.example.guysdestiny.services.apiModels.contact.ContactListRequest
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse
import kotlinx.android.synthetic.main.fragment_contact_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ContactList : Fragment() {
    val apiClient = APIClient()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val contactRequest = ContactListRequest()
        contactRequest.api_key = "c95332ee022df8c953ce470261efc695ecf3e784"
        contactRequest.uid = "256"
        val contacts = ArrayList<ContactData>()
        getContactList(contactRequest, contacts)
        super.onViewCreated(view, savedInstanceState)
    }

    fun getContactList(request: ContactListRequest, contacts: ArrayList<ContactData>) {

        val call: Call<List<ContactListResponse>> = apiClient.prepareRetrofit(true).getContactList(request)

        call.enqueue(object : Callback<List<ContactListResponse>> {
            override fun onFailure(call: Call<List<ContactListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ContactListResponse>>,
                response: Response<List<ContactListResponse>>
            ) {
                val res: List<ContactListResponse> = response.body()!!
                for(item in res){
                    contacts.add(ContactData(item.name, item.id))
                }
                rv_contact_list?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = ContactAdapter(contacts)
                }
            }
        })
    }
}
