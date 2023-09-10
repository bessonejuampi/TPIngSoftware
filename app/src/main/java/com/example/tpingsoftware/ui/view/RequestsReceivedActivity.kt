package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityRequestsReceivedBinding
import com.example.tpingsoftware.ui.viewModels.RequestReceivedViewModel
import com.example.tpingsoftware.utils.AppPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestsReceivedActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRequestsReceivedBinding

    private val viewModel : RequestReceivedViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestsReceivedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getRequestReceived(AppPreferences.getUserSession(this))

        setupView()
    }

    private fun setupView() {


    }
}