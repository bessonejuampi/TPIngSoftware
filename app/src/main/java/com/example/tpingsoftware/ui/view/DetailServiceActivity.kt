package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityDeatilServiceBinding
import com.example.tpingsoftware.ui.viewModels.DetailServiceVIewModel
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.DialogHelper
import com.example.tpingsoftware.utils.TypeDialog
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeatilServiceBinding

    private var service: Service? = null

    private val viewModel: DetailServiceVIewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeatilServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null) {

            val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE_SERVICE_TO_DETAILS)
            service = bundle!!.getParcelable(Constants.KEY_SERVICE)
        }

        setupView()
        observeMutableLiveData()
    }

    private fun observeMutableLiveData() {

        viewModel.requestServiceLiveData.observe(this, Observer {

            hideLoading()
            if (it.result == TypeDialog.GO_TO_HOME) {
                DialogHelper.showResultDialog(
                    this,
                    it.title!!,
                    it.description!!,
                    { viewModel.goToHome() },
                    { viewModel.goToHome() })
            } else {
                DialogHelper.showResultDialog(this, it.title!!, it.description!!, { })
            }
        })

    }

    private fun setupView() {

        binding.tvTitle.text = service?.title
        binding.tvDescription.text = service?.description
        binding.tvLocation.text = "${service?.location}, ${service?.province}"

        if (service?.idImage != null) {

            Picasso.get().load(service!!.imageUir).into(binding.ivHeader)
        }

        binding.btnRequestService.setOnClickListener {

            showLoading()

            DialogHelper.showConfirmationDialog(
                this,
                "¿Esta seguro/a que desea solicitar este servico?",
                { viewModel.sendRequest(service!!.id, AppPreferences.getUserSession(this)) },
                { hideLoading() })
        }
    }

    private fun showLoading() {

        binding.llBody.visibility = View.GONE
        binding.lyProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {

        binding.llBody.visibility = View.VISIBLE
        binding.lyProgress.visibility = View.GONE
    }
}