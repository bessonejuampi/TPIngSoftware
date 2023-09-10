package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityRequestsReceivedBinding
import com.example.tpingsoftware.ui.view.adapters.RequestsReceivedAdapter
import com.example.tpingsoftware.ui.viewModels.RequestReceivedViewModel
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.DialogHelper
import com.example.tpingsoftware.utils.TypeDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestsReceivedActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRequestsReceivedBinding

    private val viewModel : RequestReceivedViewModel by viewModel()

    private var listRequestsReceived : List<Request>? = null
    private var listServicesFromUser : List<Service>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestsReceivedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getRequestReceived(AppPreferences.getUserSession(this))

        viewModel.getServiceFromUser(AppPreferences.getUserSession(this)!!)

        observeMutableLiveData()
    }

    private fun observeMutableLiveData() {

        viewModel.requestsReceivedLiveData.observe(this, Observer {

            listRequestsReceived = it
            if (!listServicesFromUser.isNullOrEmpty()){
                setupAdapter()
            }

        })

        viewModel.resultLiveData.observe(this, Observer {

            when(it.result){
                TypeDialog.GO_TO_HOME ->{
                    DialogHelper.showResultDialog(
                        this,
                        it.title!!,
                        it.description!!,
                        {viewModel.goToHome()},
                        {viewModel.goToHome()}
                    )
                }
                TypeDialog.DISMISS -> {
                    DialogHelper.showResultDialog(
                        this,
                        it.title!!,
                        it.description!!,
                        {}
                    )
                }
                TypeDialog.ON_HOLD -> TODO()
                TypeDialog.GO_TO_LOGIN -> TODO()
                null -> TODO()
            }


        })

        viewModel.listServiceFromUserLiveData.observe(this, Observer {

            listServicesFromUser = it
            if (!listRequestsReceived.isNullOrEmpty()){
                setupAdapter()
            }


        })
    }

    private fun setupAdapter() {

        binding.rvRequestsReceived.layoutManager = LinearLayoutManager(this)
        val adapter = RequestsReceivedAdapter(listRequestsReceived!!, listServicesFromUser!!, viewModel)
        binding.rvRequestsReceived.adapter = adapter
    }
}