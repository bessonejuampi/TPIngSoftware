package com.example.tpingsoftware.ui.view

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
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

    private var isFavorite = false

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
        viewModel.isFavoriteService(service!!.id)
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

        viewModel.isFavoriteServiceLiveData.observe(this, Observer {

            isFavorite = if (it){
                val drawable = resources.getDrawable(R.drawable.start_yellow, null)
                binding.ivFavorite.setImageDrawable(drawable)
                true
            }else{
                val drawable = resources.getDrawable(R.drawable.start_border, null)
                binding.ivFavorite.setImageDrawable(drawable)
                false
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
                { viewModel.sendRequest(service!!.id, AppPreferences.getUserSession(this), service!!.idProvider, service!!.title) },
                { hideLoading() })
        }

        binding.ivFavorite.setOnClickListener{

            if (isFavorite){

                viewModel.deleteFavorite(service!!.id)
                val drawable = resources.getDrawable(R.drawable.start_border, null)
                binding.ivFavorite.setImageDrawable(drawable)
            }else{

                viewModel.addFavorite(service!!.id)
                val drawable = resources.getDrawable(R.drawable.start_yellow, null)
                binding.ivFavorite.setImageDrawable(drawable)
            }
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