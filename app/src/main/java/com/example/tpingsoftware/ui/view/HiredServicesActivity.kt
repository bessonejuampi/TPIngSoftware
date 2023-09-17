package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityHiredServicesBinding
import com.example.tpingsoftware.ui.viewModels.HiredServicesViewModel
import com.example.tpingsoftware.utils.AppPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel

class HiredServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHiredServicesBinding

    private val viewModel : HiredServicesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiredServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHiredServicesFromUser(AppPreferences.getUserSession(this)!!)

        observerMutableLiveData()
    }

    private fun observerMutableLiveData() {

        viewModel.hiredServicesLiveData.observe(this, Observer {

        })
    }
}