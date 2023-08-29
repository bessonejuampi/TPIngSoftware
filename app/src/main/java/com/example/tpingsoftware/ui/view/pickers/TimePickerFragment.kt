package com.example.tpingsoftware.ui.view.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePickerFragment(
    val listener : (String) -> Unit
) :DialogFragment(), TimePickerDialog.OnTimeSetListener {


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        listener("$hourOfDay:$minute")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val canlendar = Calendar.getInstance()

        val hour = canlendar.get(Calendar.HOUR_OF_DAY)
        val minute = canlendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(activity as Context, this, hour, minute, true)

        return dialog
    }
}