package com.example.checknotification

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.checknotification.databinding.ActivityMain3Binding
import dong.duan.ecommerce.library.base.BaseActivity




class MainActivity3 : BaseActivity<ActivityMain3Binding>() {
    override fun getLayoutResourceId() = R.layout.activity_main3

    override fun getViewBinding() = ActivityMain3Binding.inflate(layoutInflater)

    val handler = Handler() { msg ->
        val bundle = msg.data
        val value = bundle.getString("x")
        binding.time.text = value
        true
    }

    var istart = false // Use a variable to track thread status


    override fun createView() {
        binding.btnStart.setOnClickListener {
            istart = !istart // Toggle the istart variable
            if (istart) {
                Thread(object : Runnable {
                    override fun run() {
                        var i = 0
                        while (true) {
                            if (!istart) {
                                break // Exit the loop if istart is false
                            }
                            try {
                                Thread.sleep(100)
                                i++
                                val mess = Message()
                                val b = Bundle()
                                b.putString("x", i.toString())
                                mess.data = b
                                handler.sendMessage(mess)
                            } catch (e: Exception) {
                                // Handle exceptions
                            }
                        }
                    }
                }).start()
            }
        }
    }
}
