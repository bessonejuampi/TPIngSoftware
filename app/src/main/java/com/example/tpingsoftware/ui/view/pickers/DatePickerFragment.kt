package com.example.tpingsoftware.ui.view.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment(
    val listener: (day:Int, mouth:Int, year:Int) -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onDateSet(view: DatePicker?, year: Int, mouth: Int, day: Int) {
        listener(day, mouth, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(activity as Context, this, year, month, day)
        picker.datePicker.minDate = calendar.timeInMillis

        return picker
    }
}