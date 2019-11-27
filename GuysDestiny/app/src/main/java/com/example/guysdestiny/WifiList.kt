package com.example.guysdestiny


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_wifi_list.*

/**
 * A simple [Fragment] subclass.
 */
class WifiList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        button_wifi_to_posts?.setOnClickListener{
            navController.navigate(R.id.action_wifiList_to_postList)
        }
    }

}
