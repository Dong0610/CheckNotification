package com.example.checknotification

import android.os.Bundle
import android.os.Message

import com.example.checknotification.databinding.ActivityMainBinding
import dong.duan.ecommerce.library.base.BaseActivity
import java.lang.Exception


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId()=R.layout.activity_main

    override fun getViewBinding()=ActivityMainBinding.inflate(layoutInflater)


    override fun createView() {
        binding.button2.setOnClickListener {
            val a= binding.editTextNumber1.text.toString().toInt()
            val b= binding.editTextNumber2.text.toString().toInt()
            var rs= a+b
            binding.editTextNumber3.setText(rs.toString())
        }

    }


}







