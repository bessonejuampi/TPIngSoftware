package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Appreciation
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityEditMyServiceBinding
import com.example.tpingsoftware.ui.view.adapters.AppreciationsAdapter
import com.example.tpingsoftware.ui.viewModels.EditMyServiceViewModel
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.DialogHelper
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditMyServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMyServiceBinding

    private val viewModel: EditMyServiceViewModel by viewModel()

    private var service: Service? = null

    private var appreciationAreVisible = false

    private var listAppreciation = listOf<Appreciation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMyServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null) {
            val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE_SERVICE_TO_EDIT_MY_SERVICE)
            service = bundle!!.getParcelable(Constants.KEY_SERVICE)
        }

        viewModel.getAppreciations(service!!.id)
        setupView()
        observeMutableLiveData()
    }

    private fun observeMutableLiveData() {

        viewModel.resultEditServiceLiveData.observe(this, Observer {

            hideLoading()

            DialogHelper.showResultDialog(
                this,
                it.title!!,
                it.description!!,
                { viewModel.goToHome() },
                { viewModel.goToHome() })
        })

        viewModel.appreciationLiveData.observe(this, Observer {

            if (it.isNotEmpty()) {
                listAppreciation = it
                val adapter = AppreciationsAdapter(it)
                binding.rvAppreciations.adapter = adapter
                binding.tvEmptyList.visibility = View.GONE
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

        binding.btnDeleteService.setOnClickListener {

            showLoading()
            viewModel.deleteService(service!!)
        }

        binding.btnEditService.setOnClickListener {

            viewModel.goToEditService(service!!)
        }

        binding.tvAppreciations.setOnClickListener {

            if (appreciationAreVisible) {

                binding.rvAppreciations.visibility = View.GONE
                binding.llAppreciations.visibility = View.GONE
                binding.tvAppreciations.text = "Mostrar valoraciones"
                binding.tvAppreciations.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.expand_more,
                    0,
                    0,
                    0
                )

                appreciationAreVisible = false
            } else {

                binding.llAppreciations.visibility = View.VISIBLE
                if (listAppreciation.isNotEmpty()) {
                    binding.rvAppreciations.visibility = View.VISIBLE
                }
                binding.tvAppreciations.text = "Ocultar valoraciones"
                binding.tvAppreciations.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.expand_less,
                    0,
                    0,
                    0
                )
                appreciationAreVisible = true
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