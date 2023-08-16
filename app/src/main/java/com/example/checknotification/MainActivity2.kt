package com.example.checknotification

import android.content.Intent
import android.os.Bundle

import com.example.checknotification.databinding.ActivityMain2Binding
import dong.duan.ecommerce.library.base.BaseActivity


class MainActivity2 : BaseActivity <ActivityMain2Binding>(){
    override fun getLayoutResourceId()= R.layout.activity_main2

    override fun getViewBinding()= ActivityMain2Binding .inflate(layoutInflater)

    override fun createView() {
        binding.button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.button3.setOnClickListener {
            addFragment(MainFragment())
        }
    }


}



