package com.example.tpingsoftware.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityAddAvailabilityBinding
import com.example.tpingsoftware.ui.view.pickers.DatePickerFragment
import com.example.tpingsoftware.ui.viewModels.AddAvailabilityViewModel
import com.example.tpingsoftware.ui.viewModels.AddServiceViewModel
import com.example.tpingsoftware.utils.Constants
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAvailabilityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAvailabilityBinding
    private val viewModel : AddAvailabilityViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAvailabilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var service: Service? = null
        var imageSelected : Uri? = null
        val bundle = intent.getBundleExtra(Constants.KEY_EXTRAS_ADD_AVAILABILITY)
        if (bundle != null) {
            service = bundle.getParcelable(Constants.KEY_SERVICE_BUNDLE)
            imageSelected = bundle.getParcelable(Constants.KEY_IMAGE_SELECTED)
        }

        setupView()

    }

    private fun setupView() {
        binding.tilAddDAte.setOnClickListener {
            showDatePickerDialog()
        }

        binding.etAddDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {

        val datePicker = DatePickerFragment { day, mouth, year -> onDateSelected(day, mouth, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day:Int, mouth:Int, year:Int){
        binding.etAddDate.setText( "$day / $mouth / $year")
    }
}