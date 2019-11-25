package com.example.guysdestiny


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_chat.*


class Chat : Fragment() {

    private lateinit var messAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        messAdapter = ChatAdapter(activity!!.applicationContext)

        messageList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messAdapter
        }

        btnSend.setOnClickListener {
            if(txtMessage.text.isNotEmpty()) {
                val message = Message(
                    "Jano",
                    txtMessage.text.toString(),
                    "1.1.2019"
                )
                messAdapter.addMessage(message)
                resetInput()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

     fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        val inputManager =
            activity!!.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
