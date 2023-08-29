package com.example.tpingsoftware.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.utils.Constants

class AddAvailabilityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_availability)

        var service: Service? = null
        var imageSelected : Uri? = null
        val bundle = intent.getBundleExtra(Constants.KEY_EXTRAS_ADD_AVAILABILITY)
        if (bundle != null) {
            service = bundle.getParcelable(Constants.KEY_SERVICE_BUNDLE)
            imageSelected = bundle.getParcelable(Constants.KEY_IMAGE_SELECTED)
        }

        Log.i("", "")
    }
}