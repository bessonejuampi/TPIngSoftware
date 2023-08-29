package com.example.tpingsoftware.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityAddAvailabilityBinding
import com.example.tpingsoftware.ui.view.adapters.AddAvailabilityAdapter
import com.example.tpingsoftware.ui.view.pickers.DatePickerFragment
import com.example.tpingsoftware.ui.view.pickers.TimePickerFragment
import com.example.tpingsoftware.ui.viewModels.AddAvailabilityViewModel
import com.example.tpingsoftware.ui.viewModels.AddServiceViewModel
import com.example.tpingsoftware.utils.Constants
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAvailabilityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAvailabilityBinding
    private val viewModel : AddAvailabilityViewModel by viewModel()
    private val listItems : ArrayList<Availability> = arrayListOf()
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
            binding.tilAddDAte.error = null
            showDatePickerDialog()
        }

        binding.etAddDate.setOnClickListener {
            binding.tilAddDAte.error = null
            showDatePickerDialog()
        }

        binding.etAddHour.setOnClickListener {
            binding.tilAddHour.error = null
            showTimePickerDialog()
        }

        binding.tilAddHour.setOnClickListener {
            binding.tilAddHour.error = null
            showTimePickerDialog()
        }


        binding.btnAddAvailability.setOnClickListener {

            if(binding.etAddDate.text.isNullOrEmpty()){

                binding.tilAddDAte.error = getString(R.string.text_input_mandatory)
            }

            if (binding.etAddHour.text.isNullOrEmpty()){

                binding.tilAddHour.error = getString(R.string.text_input_mandatory)
            }

            if (!binding.etAddDate.text.isNullOrEmpty() || !binding.etAddHour.text.isNullOrEmpty()){

                addItem()
            }
        }
    }

    private fun addItem() {

        val newAvailability = Availability(
            binding.etAddDate.text.toString(),
            binding.etAddHour.text.toString()
        )

        listItems.add(newAvailability)
        binding.rvAvailabilityList.layoutManager = LinearLayoutManager(this)
        val adapter = AddAvailabilityAdapter(listItems)

        binding.rvAvailabilityList.adapter = adapter
    }

    private fun showTimePickerDialog() {

        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showDatePickerDialog() {

        val datePicker = DatePickerFragment { day, mouth, year -> onDateSelected(day, mouth, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day:Int, mouth:Int, year:Int){

        binding.etAddDate.setText( "$day / $mouth / $year")
    }

    private fun onTimeSelected(time:String){

        binding.etAddHour.setText(time)
    }

}