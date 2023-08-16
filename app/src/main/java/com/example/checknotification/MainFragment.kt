package com.example.checknotification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.egame.backgrounderaser.aigenerator.base.BaseFragment
import com.example.checknotification.databinding.FragmentMainBinding

class MainFragment :BaseFragment<FragmentMainBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?)=FragmentMainBinding.inflate(layoutInflater)

    override fun initView() {
        binding.button2.setOnClickListener {
            val a= binding.editTextNumber1.text.toString().toInt()
            val b= binding.editTextNumber2.text.toString().toInt()
            var rs= a+b
            binding.editTextNumber3.setText(rs.toString())
        }

    }
}