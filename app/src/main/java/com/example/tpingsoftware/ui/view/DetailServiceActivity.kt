package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityDeatilServiceBinding
import com.example.tpingsoftware.utils.Constants
import com.squareup.picasso.Picasso

class DetailServiceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDeatilServiceBinding

    private var service : Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeatilServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null){

            val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE_SERVICE_TO_DETAILS)
            service = bundle!!.getParcelable(Constants.KEY_SERVICE)
        }
        setupView()
    }

    private fun setupView() {

        binding.tvTitle.text = service?.title
        binding.tvDescription.text = service?.description
        binding.tvLocation.text = "${service?.location}, ${service?.province}"

        if (service?.idImage != null){

            Picasso.get().load(service!!.imageUir).into(binding.ivHeader)
        }
    }
}