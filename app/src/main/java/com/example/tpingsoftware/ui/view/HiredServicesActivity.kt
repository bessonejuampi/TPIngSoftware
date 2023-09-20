package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.databinding.ActivityHiredServicesBinding
import com.example.tpingsoftware.ui.view.adapters.HiredServicesAdapter
import com.example.tpingsoftware.ui.view.adapters.RequestsReceivedAdapter
import com.example.tpingsoftware.ui.viewModels.HiredServicesViewModel
import com.example.tpingsoftware.utils.AppPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

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

            if (!it.isNullOrEmpty()){
                setupAdapter(it)
            }else{
                setupEmptyView()
            }
        })
    }

    private fun setupEmptyView() {

        binding.llEmptyView.visibility = View.VISIBLE
        binding.llBody.visibility = View.INVISIBLE
    }

    private fun setupAdapter(listRequest: ArrayList<Request>) {

        binding.rvHiredServices.layoutManager = LinearLayoutManager(this)
        val adapter = HiredServicesAdapter(listRequest)
        binding.rvHiredServices.adapter = adapter

    }
}