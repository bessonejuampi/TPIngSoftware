package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.FragmentAppreciateBinding
import com.example.tpingsoftware.databinding.FragmentMyServiceBinding

class AppreciateFragment : DialogFragment() {

    private lateinit var binding: FragmentAppreciateBinding

    private var ratingListener: ((Float, String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAppreciateBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding.submitButton.setOnClickListener {
            val rating = binding.ratingBar.rating
            val comment = binding.commentEditText.text.toString()

            ratingListener?.invoke(rating, comment)
            dismiss()
        }
    }

    fun setRatingListener(listener: (Float, String) -> Unit) {
        ratingListener = listener
    }
}